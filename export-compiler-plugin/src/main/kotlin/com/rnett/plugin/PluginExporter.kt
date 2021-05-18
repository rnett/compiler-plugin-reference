package com.rnett.plugin

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocationWithRange
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.descriptors.ReceiverParameterDescriptor
import org.jetbrains.kotlin.descriptors.TypeParameterDescriptor
import org.jetbrains.kotlin.descriptors.effectiveVisibility
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.IrAnnotationContainer
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrDeclarationBase
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.declarations.IrDeclarationWithVisibility
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrTypeAlias
import org.jetbrains.kotlin.ir.declarations.IrTypeParameter
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.path
import org.jetbrains.kotlin.ir.descriptors.toIrBasedDescriptor
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrScriptSymbol
import org.jetbrains.kotlin.ir.symbols.IrTypeParameterSymbol
import org.jetbrains.kotlin.ir.types.IrDynamicType
import org.jetbrains.kotlin.ir.types.IrErrorType
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.IdSignature
import org.jetbrains.kotlin.ir.util.constructedClass
import org.jetbrains.kotlin.ir.util.constructedClassType
import org.jetbrains.kotlin.ir.util.getAnnotation
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.hasDefaultValue
import org.jetbrains.kotlin.ir.util.isFunctionOrKFunction
import org.jetbrains.kotlin.ir.util.isVararg
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.render
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.Variance

private fun IrAnnotationContainer.shouldExport(): Boolean = hasAnnotation(Names.PluginExport)

class PluginExporter(val context: IrPluginContext, val messageCollector: MessageCollector, val export: (ExportDeclaration) -> Unit) :
    IrElementTransformerVoidWithContext() {

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

    private val IrDeclaration.publicSignature get() = symbol.signature?.asPublic()?.toSignature() ?: error("No public signature")

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
        ExportDeclaration.TypeParameter(name.asString(), index, variance.toVariance(), this.superTypes.map { it.toTypeString() })

    fun IrValueParameter.toReceiver() = ExportDeclaration.Receiver(type.typeNic, type.toTypeString())

    fun IrValueParameter.toParameter() = ExportDeclaration.Param(name.asString(), index, hasDefaultValue(), isVararg, type.toTypeString())

    fun ReceiverParameterDescriptor.toReceiver() = type.toIrType().let { ExportDeclaration.Receiver(it.typeNic, it.toTypeString()) }

    fun TypeParameterDescriptor.toTypeParameter() =
        ExportDeclaration.TypeParameter(name.asString(), index, variance.toVariance(), this.upperBounds.map { it.toIrType().toTypeString() })

    @OptIn(ObsoleteDescriptorBasedAPI::class)
    fun KotlinType.toIrType() = context.typeTranslator.translateType(this)

    private fun IrClass.getDeclaration(): ExportDeclaration.Class =
        ExportDeclaration.Class(
            resolvedName,
            publicSignature,
            typeParameters.map { it.toTypeParameter() },
            displayName
        )

    private fun IrTypeAlias.getDeclaration(): ExportDeclaration.Typealias =
        ExportDeclaration.Typealias(
            resolvedName,
            publicSignature,
            typeParameters.map { it.toTypeParameter() },
            expandedType.toTypeString(),
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
        return ExportDeclaration.Property(
            resolvedName,
            publicSignature,
            descriptor.type.toIrType().toTypeString(),
            descriptor.dispatchReceiverParameter?.toReceiver(),
            listOfNotNull(descriptor.extensionReceiverParameter).map { it.toReceiver() },
            descriptor.typeParameters.map { it.toTypeParameter() },
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