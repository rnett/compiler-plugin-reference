package com.rnett.plugin

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.builtins.PrimitiveType
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocationWithRange
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.descriptors.toIrBasedDescriptor
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrScriptSymbol
import org.jetbrains.kotlin.ir.symbols.IrTypeParameterSymbol
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.multiplatform.findExpects
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.Variance
import kotlin.collections.set


class PluginExporter(
    val context: IrPluginContext,
    val messageCollector: MessageCollector,
    val export: (ExportDeclaration) -> Unit
) :
    IrElementTransformerVoidWithContext() {

    @OptIn(ObsoleteDescriptorBasedAPI::class)
    private val IrDeclaration.isActual: Boolean
        get() = descriptor.run { this is MemberDescriptor && this.isActual || this is PropertyAccessorDescriptor && this.correspondingProperty.isActual }

    @OptIn(ObsoleteDescriptorBasedAPI::class)
    private val IrDeclaration.isExpect: Boolean
        get() = descriptor.run { this is MemberDescriptor && this.isExpect || this is PropertyAccessorDescriptor && this.correspondingProperty.isExpect }


    @OptIn(ObsoleteDescriptorBasedAPI::class)
    private fun IrDeclarationWithVisibility.shouldExport(): Boolean {
        if (this.isActual) {
            return hasAnnotation(Names.PluginExport) && descriptor.findExpects()
                .none { it.annotations.hasAnnotation(Names.PluginExport) }
        }

        if (this is IrMemberWithContainerSource && this.parentClassOrNull?.isAnnotationClass == true && this.parentAsClass.shouldExport()) {
            return this is IrProperty
        }

        return hasAnnotation(Names.PluginExport)
    }

    private fun IrElement.messageLocation(): CompilerMessageSourceLocation? {
        val range = currentFile.fileEntry.getSourceRangeInfo(this.startOffset, this.endOffset)
        return CompilerMessageLocationWithRange.create(
            currentFile.path,
            range.startLineNumber,
            range.startColumnNumber,
            range.endLineNumber,
            range.endColumnNumber,
            null
        )
    }

    fun reportError(location: IrElement, message: String) =
        messageCollector.report(CompilerMessageSeverity.ERROR, message, location.messageLocation())

    @OptIn(ObsoleteDescriptorBasedAPI::class)
    private fun IrDeclarationWithVisibility.checkVisibility() {
        if (visibility.effectiveVisibility(this.descriptor, true).publicApi)
            return

        reportError(this, "Can only use @PluginExport on public or @PublishedApi internal declarations.")
    }

    fun IdSignature.PublicSignature.toSignature() = Signature(packageFqName, declarationFqName, id, mask)

    private val IrDeclaration.publicSignature
        get() = symbol.signature?.asPublic()?.toSignature() ?: error("No public signature")

    val IrDeclarationParent.resolvedName get() = kotlinFqName.toResolvedName()

    val IrAnnotationContainer.displayName: String?
        get() {
            if (!this.hasAnnotation(Names.PluginExport))
                return null

            val call = this.getAnnotation(Names.PluginExport)!!
            if (call.valueArgumentsCount > 0) {
                val str = call.getValueArgument(0) as? IrConst<*> ?: return null
                return str.value as? String
            } else {
                return null
            }
        }

    val IrType.typeNic: String get() = listOf(this).typeNics().values.single()

    fun Iterable<IrType>.typeNics(): Map<IrType, String> {
        val classNameCounts = mutableMapOf<String, Int>()
        return associateWith {
            val name = if (it.isFunctionOrKFunction()) {
                "Lambda"
            } else {
                when (it) {
                    is IrErrorType -> error("Error type $it")
                    is IrSimpleType -> when (val cls = it.classifier) {
                        is IrClassSymbol -> cls.owner.name.identifier
                        is IrScriptSymbol -> "Script"
                        is IrTypeParameterSymbol -> cls.owner.name.identifier
                        else -> error("Unknown classifier type $cls")
                    }
                    is IrDynamicType -> "Dynamic"
                    else -> error("Unknown kind of type $it")
                }
            }

            val count = classNameCounts[name] ?: 0
            classNameCounts[name] = count + 1
            if (count == 0)
                name
            else
                name + count
        }
    }

    @OptIn(ObsoleteDescriptorBasedAPI::class)
    val IrProperty.resolvedName
        get() = descriptor.fqNameSafe.toResolvedName()

    fun IrType.toTypeString() = TypeString(this.render())

    fun Variance.toVariance() = when (this) {
        Variance.INVARIANT -> ExportDeclaration.Variance.NONE
        Variance.IN_VARIANCE -> ExportDeclaration.Variance.IN
        Variance.OUT_VARIANCE -> ExportDeclaration.Variance.OUT
    }

    fun IrTypeParameter.toTypeParameter() =
        ExportDeclaration.TypeParameter(
            name.asString(),
            index,
            variance.toVariance(),
            this.superTypes.filterNot { it.isNullableAny() }.map { it.toTypeString() })

    fun IrValueParameter.toReceiver() = ExportDeclaration.Receiver(type.typeNic, type.toTypeString())

    fun IrValueParameter.toParameter() =
        ExportDeclaration.Param(name.asString(), index, hasDefaultValue(), isVararg, type.toTypeString())

    fun ReceiverParameterDescriptor.toReceiver() =
        type.toIrType().let { ExportDeclaration.Receiver(it.typeNic, it.toTypeString()) }

    fun TypeParameterDescriptor.toTypeParameter() =
        ExportDeclaration.TypeParameter(
            name.asString(),
            index,
            variance.toVariance(),
            this.upperBounds.map { it.toIrType().toTypeString() })

    @OptIn(ObsoleteDescriptorBasedAPI::class)
    fun KotlinType.toIrType() = context.typeTranslator.translateType(this)

    fun IrConst<*>.toConstValue(): ConstantValue = when (kind) {
        IrConstKind.Null -> ConstantValue.Null
        IrConstKind.Boolean -> ConstantValue.Boolean(value as Boolean)
        IrConstKind.Char -> ConstantValue.Char(value as Char)
        IrConstKind.Byte -> ConstantValue.Byte(value as Byte)
        IrConstKind.Short -> ConstantValue.Short(value as Short)
        IrConstKind.Int -> ConstantValue.Int(value as Int)
        IrConstKind.Long -> ConstantValue.Long(value as Long)
        IrConstKind.String -> ConstantValue.String(value as String)
        IrConstKind.Float -> ConstantValue.Float(value as Float)
        IrConstKind.Double -> ConstantValue.Double(value as Double)
    }

    private fun IrType.annotationValueKind(): AnnotationArgument.Kind = when {
        isPrimitiveType() -> AnnotationArgument.Kind.Constant(
            when (getPrimitiveType()!!) {
                PrimitiveType.BOOLEAN -> ConstantValue.Kind.Boolean
                PrimitiveType.CHAR -> ConstantValue.Kind.Char
                PrimitiveType.BYTE -> ConstantValue.Kind.Byte
                PrimitiveType.SHORT -> ConstantValue.Kind.Short
                PrimitiveType.INT -> ConstantValue.Kind.Int
                PrimitiveType.FLOAT -> ConstantValue.Kind.Float
                PrimitiveType.LONG -> ConstantValue.Kind.Long
                PrimitiveType.DOUBLE -> ConstantValue.Kind.Double
            }
        )
        isString() -> AnnotationArgument.Kind.Constant(ConstantValue.Kind.String)
        isKClass() -> AnnotationArgument.Kind.ClassRef
        classOrNull?.owner?.isEnumClass == true -> if (classOrNull!!.owner.shouldExport())
            AnnotationArgument.Kind.ExportedEnum(classFqName!!.toResolvedName())
        else
            AnnotationArgument.Kind.OpaqueEnum
        classOrNull?.owner?.isAnnotationClass == true -> if (classOrNull!!.owner.shouldExport())
            AnnotationArgument.Kind.ExportedAnnotation(classFqName!!.toResolvedName())
        else
            AnnotationArgument.Kind.OpaqueAnnotation(classFqName!!.toResolvedName())
        isArray() -> AnnotationArgument.Kind.Array(getArrayElementType(context.irBuiltIns).annotationValueKind())
        isPrimitiveArray() -> AnnotationArgument.Kind.Array(getArrayElementType(context.irBuiltIns).annotationValueKind())
        else -> error("Unknown array type ${render()}")
    }

    private fun IrExpression.toAnnotationArgument(): AnnotationArgument = when (this) {
        is IrConst<*> -> AnnotationArgument.Constant(toConstValue())
        is IrClassReference -> AnnotationArgument.ClassRef(classType.classFqName!!.toResolvedName())
        is IrGetEnumValue -> {
            val className = type.classFqName!!.toResolvedName()
            val name = symbol.owner.name.asString()
            val ordinal = symbol.owner.parentAsClass.declarations.filterIsInstance<IrEnumEntry>()
                .indexOfFirst { it.symbol == symbol }

            if (this.symbol.owner.parentAsClass.shouldExport())
                AnnotationArgument.ExportedEnum(
                    className,
                    name,
                    ordinal
                ) else
                AnnotationArgument.OpaqueEnum(
                    className,
                    name,
                    ordinal
                )
        }
        is IrConstructorCall -> {
            val klass = this.symbol.owner.constructedClass
            if (klass.isAnnotationClass) {
                val klassFqn = klass.resolvedName

                val arguments = symbol.owner.valueParameters.associate {
                    it.name.asString() to getValueArgument(it.index)
                }
                    .filterValues { it != null }
                    .mapValues { it.value!!.toAnnotationArgument() }

                if (klass.shouldExport())
                    AnnotationArgument.ExportedAnnotation(klassFqn, arguments)
                else
                    AnnotationArgument.OpaqueAnnotation(klassFqn, arguments)
            } else {
                error("Unknown annotation argument ${dumpKotlinLike()}")
            }
        }
        is IrVararg -> AnnotationArgument.Array(elements.map {
            if (it is IrExpression)
                it.toAnnotationArgument()
            else
                error("Unsupported spread operator in annotation value")
        }, varargElementType.annotationValueKind())
        else -> error("Unknown annotation argument ${dumpKotlinLike()}")
    }

    private fun IrClass.getDeclaration(): ExportDeclaration.Class {
        val enumNames = if (isEnumClass) {
            this.declarations.filterIsInstance<IrEnumEntry>()
                .map { it.name.asString() to it.publicSignature }
        } else null

        val annotationParams = if (isAnnotationClass) {
            val constructor = primaryConstructor!!
            constructor.valueParameters.associate {
                val defaultValue = it.defaultValue?.expression
                it.name.asString() to AnnotationParameter(
                    it.index,
                    it.type.annotationValueKind(),
                    defaultValue?.toAnnotationArgument()
                )
            }
        } else null

        return ExportDeclaration.Class(
            resolvedName,
            publicSignature,
            typeParameters.map { it.toTypeParameter() },
            enumNames,
            annotationParams,
            displayName
        )
    }

    private fun IrTypeAlias.getDeclaration(): ExportDeclaration.Typealias =
        ExportDeclaration.Typealias(
            resolvedName,
            publicSignature,
            typeParameters.map { it.toTypeParameter() },
            displayName
        )

    private fun IrSimpleFunction.getDeclaration(): ExportDeclaration.Function =
        ExportDeclaration.Function(
            resolvedName,
            publicSignature,
            returnType.toTypeString(),
            dispatchReceiverParameter?.toReceiver(),
            listOfNotNull(extensionReceiverParameter).map { it.toReceiver() },
            typeParameters.map { it.toTypeParameter() },
            valueParameters.map { it.toParameter() },
            displayName,
        )

    private fun IrProperty.getDeclaration(): ExportDeclaration.Property {
        val descriptor = toIrBasedDescriptor()

        val constValue: IrConst<*>? = if (isConst) {
            backingField?.initializer?.expression as? IrConst<*>
        } else null

        return ExportDeclaration.Property(
            resolvedName,
            publicSignature,
            descriptor.type.toIrType().toTypeString(),
            descriptor.dispatchReceiverParameter?.toReceiver(),
            listOfNotNull(descriptor.extensionReceiverParameter).map { it.toReceiver() },
            descriptor.typeParameters.map { it.toTypeParameter() },
            getter != null,
            setter != null,
            backingField != null,
            constValue?.toConstValue(),
            displayName
        )
    }

    private fun IrConstructor.getDeclaration(): ExportDeclaration.Constructor =
        ExportDeclaration.Constructor(
            resolvedName,
            publicSignature,
            this.constructedClassType.toTypeString(),
            constructedClass.typeParameters.map { it.toTypeParameter() },
            valueParameters.map { it.toParameter() },
            displayName
        )


    private fun tryExport(declaration: IrDeclarationWithVisibility) {
        if (declaration.shouldExport()) {
            declaration.checkVisibility()

            when (declaration) {
                is IrClass -> export(declaration.getDeclaration())
                is IrTypeAlias -> export(declaration.getDeclaration())
                is IrSimpleFunction -> export(declaration.getDeclaration())
                is IrProperty -> export(declaration.getDeclaration())
                is IrConstructor -> export(declaration.getDeclaration())
            }
        }
    }

    override fun visitDeclaration(declaration: IrDeclarationBase): IrStatement {
        if (declaration is IrDeclarationWithVisibility) {
            tryExport(declaration)
        }
        return super.visitDeclaration(declaration)
    }
}