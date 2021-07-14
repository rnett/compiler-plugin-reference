// GENERATED, DO NOT EDIT
package test.generation

import com.rnett.plugin.ClassReference
import com.rnett.plugin.ConstructorReference
import com.rnett.plugin.EnumEntryReference
import com.rnett.plugin.EnumInstance
import com.rnett.plugin.OpaqueAnnotationInstance
import com.rnett.plugin.OpaqueConstant
import com.rnett.plugin.PropertyReference
import com.rnett.plugin.ResolvedClass
import com.rnett.plugin.ResolvedConstructor
import com.rnett.plugin.ResolvedEnumEntry
import com.rnett.plugin.ResolvedProperty
import com.rnett.plugin.referenceEnumEntry
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irCallConstructor
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrClassReference
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetEnumValue
import org.jetbrains.kotlin.ir.expressions.IrVararg
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol
import org.jetbrains.kotlin.ir.symbols.IrEnumEntrySymbol
import org.jetbrains.kotlin.ir.symbols.IrFieldSymbol
import org.jetbrains.kotlin.ir.symbols.IrPropertySymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.IdSignature
import org.jetbrains.kotlin.ir.util.constructedClassType
import org.jetbrains.kotlin.ir.util.hasEqualFqName
import org.jetbrains.kotlin.ir.util.isGetter
import org.jetbrains.kotlin.ir.util.isSetter
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.utils.addToStdlib.cast
import test.generation.Names.tester.second.TestEnum.Instance

public class Names(
    private val _context: IrPluginContext
) {
    public fun tester(): tester = tester(_context)

    public class tester(
        private val _context: IrPluginContext
    ) {
        public fun second(): second = second(_context)

        public class second(
            private val _context: IrPluginContext
        ) {
            public fun ExportedAnnotation(): ExportedAnnotation = ExportedAnnotation(_context)

            public fun TestAnnotation(): TestAnnotation = TestAnnotation(_context)

            public fun TestClass(): TestClass = TestClass(_context)

            public fun TestEnum(): TestEnum = TestEnum(_context)

            public fun testConst(): testConst = testConst(_context)

            /**
             * Resolved reference to `tester.second.ExportedAnnotation`
             *
             */
            public class ExportedAnnotation private constructor(
                private val _context: IrPluginContext,
                symbol: IrClassSymbol
            ) : ResolvedClass(symbol, fqName) {
                /**
                 * Get the class's type.
                 */
                public val type: IrSimpleType = owner.typeWith()

                public constructor(context: IrPluginContext) : this(context, resolveSymbol(context))

                public fun i(): i = i(_context)

                public data class Instance(
                    public val i: Int
                ) {
                    public constructor(`annotation`: IrConstructorCall, context: IrPluginContext) :
                            this(i = annotation.getValueArgument(0)!!.cast<IrConst<Int>>().let {
                                it.value
                            }
                            )
                }

                /**
                 * Resolved reference to `tester.second.ExportedAnnotation.i`
                 *
                 * Dispatch receiver: `tester.second.ExportedAnnotation`
                 *
                 * Type: `kotlin.Int`
                 */
                public class i private constructor(
                    private val _context: IrPluginContext,
                    symbol: IrPropertySymbol
                ) : ResolvedProperty(symbol, fqName) {
                    /**
                     * Get the property's type.
                     */
                    public val type: IrType = (owner.getter?.returnType ?: owner.backingField?.type)!!

                    /**
                     * The getter
                     */
                    public val getter: IrSimpleFunctionSymbol = owner.getter!!.symbol

                    /**
                     * The backing field
                     */
                    public val backingField: IrFieldSymbol = owner.backingField!!.symbol

                    public constructor(context: IrPluginContext) : this(
                        context,
                        resolveSymbol(context)
                    )

                    /**
                     * Call the getter
                     *
                     * @param dispatchReceiver `tester.second.ExportedAnnotation`
                     * @return `kotlin.Int`
                     */
                    public fun `get`(builder: IrBuilderWithScope, dispatchReceiver: IrExpression):
                            IrCall = builder.irCall(getter).apply {
                        this.dispatchReceiver = dispatchReceiver
                        type = getter.owner.returnType
                    }


                    public open class Instance(
                        public val call: IrCall
                    ) {
                        init {
                            val signature = call.symbol.owner.correspondingPropertySymbol?.signature
                            val requiredSignature = i.signature
                            require(signature == requiredSignature) {
                                """Instance's signature $signature did not match the required signature of $requiredSignature"""
                            }
                        }

                        public val dispatchReceiver: IrExpression?
                            get() = call.dispatchReceiver

                        public val `property`: IrProperty
                            get() = call.symbol.owner.correspondingPropertySymbol!!.owner
                    }

                    public class GetterInstance(
                        call: IrCall
                    ) : Instance(call) {
                        init {
                            require(call.symbol.owner.isGetter) {
                                """Instance ${call.symbol} is not the right type of accessor, expected a getter"""
                            }
                        }
                    }

                    public companion object Reference :
                        PropertyReference<i>(
                            FqName("tester.second.ExportedAnnotation.i"),
                            IdSignature.PublicSignature(
                                "tester.second", "ExportedAnnotation.i",
                                5014384761142332495, 0
                            )
                        ) {
                        public override fun getResolvedReference(
                            context: IrPluginContext,
                            symbol: IrPropertySymbol
                        ): i = i(context, symbol)

                        public fun accessorInstance(call: IrCall): Instance = Instance(call)

                        public fun accessorInstanceOrNull(call: IrCall): Instance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                i.signature
                            ) {
                                return Instance(call)
                            } else {
                                return null
                            }
                        }

                        public fun getterInstance(call: IrCall): GetterInstance =
                            GetterInstance(call)

                        public fun getterInstanceOrNull(call: IrCall): GetterInstance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                i.signature && call.symbol.owner.isGetter
                            ) {
                                return GetterInstance(call)
                            } else {
                                return null
                            }
                        }
                    }
                }

                public companion object Reference :
                    ClassReference<ExportedAnnotation>(
                        FqName("tester.second.ExportedAnnotation"),
                        IdSignature.PublicSignature("tester.second", "ExportedAnnotation", null, 0)
                    ) {
                    public val i: i.Reference = ExportedAnnotation.i.Reference

                    public override fun getResolvedReference(
                        context: IrPluginContext,
                        symbol: IrClassSymbol
                    ): ExportedAnnotation = ExportedAnnotation(
                        context,
                        symbol
                    )
                }
            }

            /**
             * Resolved reference to `tester.second.TestAnnotation`
             *
             */
            public class TestAnnotation private constructor(
                private val _context: IrPluginContext,
                symbol: IrClassSymbol
            ) : ResolvedClass(symbol, fqName) {
                /**
                 * Get the class's type.
                 */
                public val type: IrSimpleType = owner.typeWith()

                public constructor(context: IrPluginContext) : this(context, resolveSymbol(context))

                public fun a(): a = a(_context)

                public fun c(): c = c(_context)

                public fun e(): e = e(_context)

                public fun n(): n = n(_context)

                public fun r(): r = r(_context)

                public fun s(): s = s(_context)

                public fun t(): t = t(_context)

                public data class Instance(
                    public val t: Int,
                    public val s: String,
                    public val e: IrEnumEntrySymbol,
                    public val c: IrClassSymbol,
                    public val r: ExportedAnnotation.Instance,
                    public val n: OpaqueAnnotationInstance,
                    public val a: List<Int>
                ) {
                    public constructor(`annotation`: IrConstructorCall, context: IrPluginContext) :
                            this(t = annotation.getValueArgument(0)?.cast<IrConst<Int>>()?.let {
                                it.value
                            } ?: 2,
                                s = annotation.getValueArgument(1)?.cast<IrConst<String>>()?.let { it.value }
                                    ?: "test",
                                e = annotation.getValueArgument(2)?.cast<IrGetEnumValue>()?.let { it.symbol }
                                    ?: context.referenceEnumEntry(
                                        FqName("tester.second.TestEnum"),
                                        Name.identifier("Two")
                                    )!!,
                                c = annotation.getValueArgument(3)?.cast<IrClassReference>()?.let {
                                    it.symbol
                                            as IrClassSymbol
                                } ?: context.referenceClass(FqName("tester.second.TestClass"))!!,
                                r = annotation.getValueArgument(4)?.cast<IrConstructorCall>()?.let {
                                    ExportedAnnotation.Instance(it, context)
                                } ?: ExportedAnnotation.Instance(i = 2),
                                n = annotation.getValueArgument(5)?.cast<IrConstructorCall>()?.let {
                                    OpaqueAnnotationInstance(it)
                                } ?: OpaqueAnnotationInstance(
                                    FqName("tester.second.NonExported"), mapOf(
                                        "s"
                                                to OpaqueConstant("test")
                                    )
                                ),
                                a = annotation.getValueArgument(6)?.cast<IrVararg>()?.let {
                                    it.elements.map {
                                        it!!.cast<IrConst<Int>>().let { it.value }
                                    }
                                } ?: listOf(1, 2, 3)
                            )
                }

                /**
                 * Resolved reference to `tester.second.TestAnnotation.a`
                 *
                 * Dispatch receiver: `tester.second.TestAnnotation`
                 *
                 * Type: `kotlin.IntArray`
                 */
                public class a private constructor(
                    private val _context: IrPluginContext,
                    symbol: IrPropertySymbol
                ) : ResolvedProperty(symbol, fqName) {
                    /**
                     * Get the property's type.
                     */
                    public val type: IrType = (owner.getter?.returnType ?: owner.backingField?.type)!!

                    /**
                     * The getter
                     */
                    public val getter: IrSimpleFunctionSymbol = owner.getter!!.symbol

                    /**
                     * The backing field
                     */
                    public val backingField: IrFieldSymbol = owner.backingField!!.symbol

                    public constructor(context: IrPluginContext) : this(
                        context,
                        resolveSymbol(context)
                    )

                    /**
                     * Call the getter
                     *
                     * @param dispatchReceiver `tester.second.TestAnnotation`
                     * @return `kotlin.IntArray`
                     */
                    public fun `get`(builder: IrBuilderWithScope, dispatchReceiver: IrExpression):
                            IrCall = builder.irCall(getter).apply {
                        this.dispatchReceiver = dispatchReceiver
                        type = getter.owner.returnType
                    }


                    public open class Instance(
                        public val call: IrCall
                    ) {
                        init {
                            val signature = call.symbol.owner.correspondingPropertySymbol?.signature
                            val requiredSignature = a.signature
                            require(signature == requiredSignature) {
                                """Instance's signature $signature did not match the required signature of $requiredSignature"""
                            }
                        }

                        public val dispatchReceiver: IrExpression?
                            get() = call.dispatchReceiver

                        public val `property`: IrProperty
                            get() = call.symbol.owner.correspondingPropertySymbol!!.owner
                    }

                    public class GetterInstance(
                        call: IrCall
                    ) : Instance(call) {
                        init {
                            require(call.symbol.owner.isGetter) {
                                """Instance ${call.symbol} is not the right type of accessor, expected a getter"""
                            }
                        }
                    }

                    public companion object Reference :
                        PropertyReference<a>(
                            FqName("tester.second.TestAnnotation.a"),
                            IdSignature.PublicSignature(
                                "tester.second",
                                "TestAnnotation.a", -1200697420457237799, 0
                            )
                        ) {
                        public override fun getResolvedReference(
                            context: IrPluginContext,
                            symbol: IrPropertySymbol
                        ): a = a(context, symbol)

                        public fun accessorInstance(call: IrCall): Instance = Instance(call)

                        public fun accessorInstanceOrNull(call: IrCall): Instance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                a.signature
                            ) {
                                return Instance(call)
                            } else {
                                return null
                            }
                        }

                        public fun getterInstance(call: IrCall): GetterInstance =
                            GetterInstance(call)

                        public fun getterInstanceOrNull(call: IrCall): GetterInstance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                a.signature && call.symbol.owner.isGetter
                            ) {
                                return GetterInstance(call)
                            } else {
                                return null
                            }
                        }
                    }
                }

                /**
                 * Resolved reference to `tester.second.TestAnnotation.c`
                 *
                 * Dispatch receiver: `tester.second.TestAnnotation`
                 *
                 * Type: `kotlin.reflect.KClass<*>`
                 */
                public class c private constructor(
                    private val _context: IrPluginContext,
                    symbol: IrPropertySymbol
                ) : ResolvedProperty(symbol, fqName) {
                    /**
                     * Get the property's type.
                     */
                    public val type: IrType = (owner.getter?.returnType ?: owner.backingField?.type)!!

                    /**
                     * The getter
                     */
                    public val getter: IrSimpleFunctionSymbol = owner.getter!!.symbol

                    /**
                     * The backing field
                     */
                    public val backingField: IrFieldSymbol = owner.backingField!!.symbol

                    public constructor(context: IrPluginContext) : this(
                        context,
                        resolveSymbol(context)
                    )

                    /**
                     * Call the getter
                     *
                     * @param dispatchReceiver `tester.second.TestAnnotation`
                     * @return `kotlin.reflect.KClass<*>`
                     */
                    public fun `get`(builder: IrBuilderWithScope, dispatchReceiver: IrExpression):
                            IrCall = builder.irCall(getter).apply {
                        this.dispatchReceiver = dispatchReceiver
                        type = getter.owner.returnType
                    }


                    public open class Instance(
                        public val call: IrCall
                    ) {
                        init {
                            val signature = call.symbol.owner.correspondingPropertySymbol?.signature
                            val requiredSignature = c.signature
                            require(signature == requiredSignature) {
                                """Instance's signature $signature did not match the required signature of $requiredSignature"""
                            }
                        }

                        public val dispatchReceiver: IrExpression?
                            get() = call.dispatchReceiver

                        public val `property`: IrProperty
                            get() = call.symbol.owner.correspondingPropertySymbol!!.owner
                    }

                    public class GetterInstance(
                        call: IrCall
                    ) : Instance(call) {
                        init {
                            require(call.symbol.owner.isGetter) {
                                """Instance ${call.symbol} is not the right type of accessor, expected a getter"""
                            }
                        }
                    }

                    public companion object Reference :
                        PropertyReference<c>(
                            FqName("tester.second.TestAnnotation.c"),
                            IdSignature.PublicSignature(
                                "tester.second",
                                "TestAnnotation.c", -4416962153448040627, 0
                            )
                        ) {
                        public override fun getResolvedReference(
                            context: IrPluginContext,
                            symbol: IrPropertySymbol
                        ): c = c(context, symbol)

                        public fun accessorInstance(call: IrCall): Instance = Instance(call)

                        public fun accessorInstanceOrNull(call: IrCall): Instance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                c.signature
                            ) {
                                return Instance(call)
                            } else {
                                return null
                            }
                        }

                        public fun getterInstance(call: IrCall): GetterInstance =
                            GetterInstance(call)

                        public fun getterInstanceOrNull(call: IrCall): GetterInstance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                c.signature && call.symbol.owner.isGetter
                            ) {
                                return GetterInstance(call)
                            } else {
                                return null
                            }
                        }
                    }
                }

                /**
                 * Resolved reference to `tester.second.TestAnnotation.e`
                 *
                 * Dispatch receiver: `tester.second.TestAnnotation`
                 *
                 * Type: `tester.second.TestEnum`
                 */
                public class e private constructor(
                    private val _context: IrPluginContext,
                    symbol: IrPropertySymbol
                ) : ResolvedProperty(symbol, fqName) {
                    /**
                     * Get the property's type.
                     */
                    public val type: IrType = (owner.getter?.returnType ?: owner.backingField?.type)!!

                    /**
                     * The getter
                     */
                    public val getter: IrSimpleFunctionSymbol = owner.getter!!.symbol

                    /**
                     * The backing field
                     */
                    public val backingField: IrFieldSymbol = owner.backingField!!.symbol

                    public constructor(context: IrPluginContext) : this(
                        context,
                        resolveSymbol(context)
                    )

                    /**
                     * Call the getter
                     *
                     * @param dispatchReceiver `tester.second.TestAnnotation`
                     * @return `tester.second.TestEnum`
                     */
                    public fun `get`(builder: IrBuilderWithScope, dispatchReceiver: IrExpression):
                            IrCall = builder.irCall(getter).apply {
                        this.dispatchReceiver = dispatchReceiver
                        type = getter.owner.returnType
                    }


                    public open class Instance(
                        public val call: IrCall
                    ) {
                        init {
                            val signature = call.symbol.owner.correspondingPropertySymbol?.signature
                            val requiredSignature = e.signature
                            require(signature == requiredSignature) {
                                """Instance's signature $signature did not match the required signature of $requiredSignature"""
                            }
                        }

                        public val dispatchReceiver: IrExpression?
                            get() = call.dispatchReceiver

                        public val `property`: IrProperty
                            get() = call.symbol.owner.correspondingPropertySymbol!!.owner
                    }

                    public class GetterInstance(
                        call: IrCall
                    ) : Instance(call) {
                        init {
                            require(call.symbol.owner.isGetter) {
                                """Instance ${call.symbol} is not the right type of accessor, expected a getter"""
                            }
                        }
                    }

                    public companion object Reference :
                        PropertyReference<e>(
                            FqName("tester.second.TestAnnotation.e"),
                            IdSignature.PublicSignature(
                                "tester.second",
                                "TestAnnotation.e", -5812214850253973038, 0
                            )
                        ) {
                        public override fun getResolvedReference(
                            context: IrPluginContext,
                            symbol: IrPropertySymbol
                        ): e = e(context, symbol)

                        public fun accessorInstance(call: IrCall): Instance = Instance(call)

                        public fun accessorInstanceOrNull(call: IrCall): Instance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                e.signature
                            ) {
                                return Instance(call)
                            } else {
                                return null
                            }
                        }

                        public fun getterInstance(call: IrCall): GetterInstance =
                            GetterInstance(call)

                        public fun getterInstanceOrNull(call: IrCall): GetterInstance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                e.signature && call.symbol.owner.isGetter
                            ) {
                                return GetterInstance(call)
                            } else {
                                return null
                            }
                        }
                    }
                }

                /**
                 * Resolved reference to `tester.second.TestAnnotation.n`
                 *
                 * Dispatch receiver: `tester.second.TestAnnotation`
                 *
                 * Type: `tester.second.NonExported`
                 */
                public class n private constructor(
                    private val _context: IrPluginContext,
                    symbol: IrPropertySymbol
                ) : ResolvedProperty(symbol, fqName) {
                    /**
                     * Get the property's type.
                     */
                    public val type: IrType = (owner.getter?.returnType ?: owner.backingField?.type)!!

                    /**
                     * The getter
                     */
                    public val getter: IrSimpleFunctionSymbol = owner.getter!!.symbol

                    /**
                     * The backing field
                     */
                    public val backingField: IrFieldSymbol = owner.backingField!!.symbol

                    public constructor(context: IrPluginContext) : this(
                        context,
                        resolveSymbol(context)
                    )

                    /**
                     * Call the getter
                     *
                     * @param dispatchReceiver `tester.second.TestAnnotation`
                     * @return `tester.second.NonExported`
                     */
                    public fun `get`(builder: IrBuilderWithScope, dispatchReceiver: IrExpression):
                            IrCall = builder.irCall(getter).apply {
                        this.dispatchReceiver = dispatchReceiver
                        type = getter.owner.returnType
                    }


                    public open class Instance(
                        public val call: IrCall
                    ) {
                        init {
                            val signature = call.symbol.owner.correspondingPropertySymbol?.signature
                            val requiredSignature = n.signature
                            require(signature == requiredSignature) {
                                """Instance's signature $signature did not match the required signature of $requiredSignature"""
                            }
                        }

                        public val dispatchReceiver: IrExpression?
                            get() = call.dispatchReceiver

                        public val `property`: IrProperty
                            get() = call.symbol.owner.correspondingPropertySymbol!!.owner
                    }

                    public class GetterInstance(
                        call: IrCall
                    ) : Instance(call) {
                        init {
                            require(call.symbol.owner.isGetter) {
                                """Instance ${call.symbol} is not the right type of accessor, expected a getter"""
                            }
                        }
                    }

                    public companion object Reference :
                        PropertyReference<n>(
                            FqName("tester.second.TestAnnotation.n"),
                            IdSignature.PublicSignature(
                                "tester.second",
                                "TestAnnotation.n", -1061662854873377138, 0
                            )
                        ) {
                        public override fun getResolvedReference(
                            context: IrPluginContext,
                            symbol: IrPropertySymbol
                        ): n = n(context, symbol)

                        public fun accessorInstance(call: IrCall): Instance = Instance(call)

                        public fun accessorInstanceOrNull(call: IrCall): Instance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                n.signature
                            ) {
                                return Instance(call)
                            } else {
                                return null
                            }
                        }

                        public fun getterInstance(call: IrCall): GetterInstance =
                            GetterInstance(call)

                        public fun getterInstanceOrNull(call: IrCall): GetterInstance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                n.signature && call.symbol.owner.isGetter
                            ) {
                                return GetterInstance(call)
                            } else {
                                return null
                            }
                        }
                    }
                }

                /**
                 * Resolved reference to `tester.second.TestAnnotation.r`
                 *
                 * Dispatch receiver: `tester.second.TestAnnotation`
                 *
                 * Type: `tester.second.ExportedAnnotation`
                 */
                public class r private constructor(
                    private val _context: IrPluginContext,
                    symbol: IrPropertySymbol
                ) : ResolvedProperty(symbol, fqName) {
                    /**
                     * Get the property's type.
                     */
                    public val type: IrType = (owner.getter?.returnType ?: owner.backingField?.type)!!

                    /**
                     * The getter
                     */
                    public val getter: IrSimpleFunctionSymbol = owner.getter!!.symbol

                    /**
                     * The backing field
                     */
                    public val backingField: IrFieldSymbol = owner.backingField!!.symbol

                    public constructor(context: IrPluginContext) : this(
                        context,
                        resolveSymbol(context)
                    )

                    /**
                     * Call the getter
                     *
                     * @param dispatchReceiver `tester.second.TestAnnotation`
                     * @return `tester.second.ExportedAnnotation`
                     */
                    public fun `get`(builder: IrBuilderWithScope, dispatchReceiver: IrExpression):
                            IrCall = builder.irCall(getter).apply {
                        this.dispatchReceiver = dispatchReceiver
                        type = getter.owner.returnType
                    }


                    public open class Instance(
                        public val call: IrCall
                    ) {
                        init {
                            val signature = call.symbol.owner.correspondingPropertySymbol?.signature
                            val requiredSignature = r.signature
                            require(signature == requiredSignature) {
                                """Instance's signature $signature did not match the required signature of $requiredSignature"""
                            }
                        }

                        public val dispatchReceiver: IrExpression?
                            get() = call.dispatchReceiver

                        public val `property`: IrProperty
                            get() = call.symbol.owner.correspondingPropertySymbol!!.owner
                    }

                    public class GetterInstance(
                        call: IrCall
                    ) : Instance(call) {
                        init {
                            require(call.symbol.owner.isGetter) {
                                """Instance ${call.symbol} is not the right type of accessor, expected a getter"""
                            }
                        }
                    }

                    public companion object Reference :
                        PropertyReference<r>(
                            FqName("tester.second.TestAnnotation.r"),
                            IdSignature.PublicSignature(
                                "tester.second",
                                "TestAnnotation.r", -8117627916896159533, 0
                            )
                        ) {
                        public override fun getResolvedReference(
                            context: IrPluginContext,
                            symbol: IrPropertySymbol
                        ): r = r(context, symbol)

                        public fun accessorInstance(call: IrCall): Instance = Instance(call)

                        public fun accessorInstanceOrNull(call: IrCall): Instance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                r.signature
                            ) {
                                return Instance(call)
                            } else {
                                return null
                            }
                        }

                        public fun getterInstance(call: IrCall): GetterInstance =
                            GetterInstance(call)

                        public fun getterInstanceOrNull(call: IrCall): GetterInstance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                r.signature && call.symbol.owner.isGetter
                            ) {
                                return GetterInstance(call)
                            } else {
                                return null
                            }
                        }
                    }
                }

                /**
                 * Resolved reference to `tester.second.TestAnnotation.s`
                 *
                 * Dispatch receiver: `tester.second.TestAnnotation`
                 *
                 * Type: `kotlin.String`
                 */
                public class s private constructor(
                    private val _context: IrPluginContext,
                    symbol: IrPropertySymbol
                ) : ResolvedProperty(symbol, fqName) {
                    /**
                     * Get the property's type.
                     */
                    public val type: IrType = (owner.getter?.returnType ?: owner.backingField?.type)!!

                    /**
                     * The getter
                     */
                    public val getter: IrSimpleFunctionSymbol = owner.getter!!.symbol

                    /**
                     * The backing field
                     */
                    public val backingField: IrFieldSymbol = owner.backingField!!.symbol

                    public constructor(context: IrPluginContext) : this(
                        context,
                        resolveSymbol(context)
                    )

                    /**
                     * Call the getter
                     *
                     * @param dispatchReceiver `tester.second.TestAnnotation`
                     * @return `kotlin.String`
                     */
                    public fun `get`(builder: IrBuilderWithScope, dispatchReceiver: IrExpression):
                            IrCall = builder.irCall(getter).apply {
                        this.dispatchReceiver = dispatchReceiver
                        type = getter.owner.returnType
                    }


                    public open class Instance(
                        public val call: IrCall
                    ) {
                        init {
                            val signature = call.symbol.owner.correspondingPropertySymbol?.signature
                            val requiredSignature = s.signature
                            require(signature == requiredSignature) {
                                """Instance's signature $signature did not match the required signature of $requiredSignature"""
                            }
                        }

                        public val dispatchReceiver: IrExpression?
                            get() = call.dispatchReceiver

                        public val `property`: IrProperty
                            get() = call.symbol.owner.correspondingPropertySymbol!!.owner
                    }

                    public class GetterInstance(
                        call: IrCall
                    ) : Instance(call) {
                        init {
                            require(call.symbol.owner.isGetter) {
                                """Instance ${call.symbol} is not the right type of accessor, expected a getter"""
                            }
                        }
                    }

                    public companion object Reference :
                        PropertyReference<s>(
                            FqName("tester.second.TestAnnotation.s"),
                            IdSignature.PublicSignature(
                                "tester.second", "TestAnnotation.s",
                                7217541905509134881, 0
                            )
                        ) {
                        public override fun getResolvedReference(
                            context: IrPluginContext,
                            symbol: IrPropertySymbol
                        ): s = s(context, symbol)

                        public fun accessorInstance(call: IrCall): Instance = Instance(call)

                        public fun accessorInstanceOrNull(call: IrCall): Instance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                s.signature
                            ) {
                                return Instance(call)
                            } else {
                                return null
                            }
                        }

                        public fun getterInstance(call: IrCall): GetterInstance =
                            GetterInstance(call)

                        public fun getterInstanceOrNull(call: IrCall): GetterInstance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                s.signature && call.symbol.owner.isGetter
                            ) {
                                return GetterInstance(call)
                            } else {
                                return null
                            }
                        }
                    }
                }

                /**
                 * Resolved reference to `tester.second.TestAnnotation.t`
                 *
                 * Dispatch receiver: `tester.second.TestAnnotation`
                 *
                 * Type: `kotlin.Int`
                 */
                public class t private constructor(
                    private val _context: IrPluginContext,
                    symbol: IrPropertySymbol
                ) : ResolvedProperty(symbol, fqName) {
                    /**
                     * Get the property's type.
                     */
                    public val type: IrType = (owner.getter?.returnType ?: owner.backingField?.type)!!

                    /**
                     * The getter
                     */
                    public val getter: IrSimpleFunctionSymbol = owner.getter!!.symbol

                    /**
                     * The backing field
                     */
                    public val backingField: IrFieldSymbol = owner.backingField!!.symbol

                    public constructor(context: IrPluginContext) : this(
                        context,
                        resolveSymbol(context)
                    )

                    /**
                     * Call the getter
                     *
                     * @param dispatchReceiver `tester.second.TestAnnotation`
                     * @return `kotlin.Int`
                     */
                    public fun `get`(builder: IrBuilderWithScope, dispatchReceiver: IrExpression):
                            IrCall = builder.irCall(getter).apply {
                        this.dispatchReceiver = dispatchReceiver
                        type = getter.owner.returnType
                    }


                    public open class Instance(
                        public val call: IrCall
                    ) {
                        init {
                            val signature = call.symbol.owner.correspondingPropertySymbol?.signature
                            val requiredSignature = t.signature
                            require(signature == requiredSignature) {
                                """Instance's signature $signature did not match the required signature of $requiredSignature"""
                            }
                        }

                        public val dispatchReceiver: IrExpression?
                            get() = call.dispatchReceiver

                        public val `property`: IrProperty
                            get() = call.symbol.owner.correspondingPropertySymbol!!.owner
                    }

                    public class GetterInstance(
                        call: IrCall
                    ) : Instance(call) {
                        init {
                            require(call.symbol.owner.isGetter) {
                                """Instance ${call.symbol} is not the right type of accessor, expected a getter"""
                            }
                        }
                    }

                    public companion object Reference :
                        PropertyReference<t>(
                            FqName("tester.second.TestAnnotation.t"),
                            IdSignature.PublicSignature(
                                "tester.second",
                                "TestAnnotation.t", -8767999791664836944, 0
                            )
                        ) {
                        public override fun getResolvedReference(
                            context: IrPluginContext,
                            symbol: IrPropertySymbol
                        ): t = t(context, symbol)

                        public fun accessorInstance(call: IrCall): Instance = Instance(call)

                        public fun accessorInstanceOrNull(call: IrCall): Instance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                t.signature
                            ) {
                                return Instance(call)
                            } else {
                                return null
                            }
                        }

                        public fun getterInstance(call: IrCall): GetterInstance =
                            GetterInstance(call)

                        public fun getterInstanceOrNull(call: IrCall): GetterInstance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                t.signature && call.symbol.owner.isGetter
                            ) {
                                return GetterInstance(call)
                            } else {
                                return null
                            }
                        }
                    }
                }

                public companion object Reference :
                    ClassReference<TestAnnotation>(
                        FqName("tester.second.TestAnnotation"),
                        IdSignature.PublicSignature("tester.second", "TestAnnotation", null, 0)
                    ) {
                    public val a: a.Reference = TestAnnotation.a.Reference

                    public val c: c.Reference = TestAnnotation.c.Reference

                    public val e: e.Reference = TestAnnotation.e.Reference

                    public val n: n.Reference = TestAnnotation.n.Reference

                    public val r: r.Reference = TestAnnotation.r.Reference

                    public val s: s.Reference = TestAnnotation.s.Reference

                    public val t: t.Reference = TestAnnotation.t.Reference

                    public override fun getResolvedReference(
                        context: IrPluginContext,
                        symbol: IrClassSymbol
                    ): TestAnnotation = TestAnnotation(context, symbol)
                }
            }

            /**
             * Resolved reference to `tester.second.TestClass`
             *
             */
            public class TestClass private constructor(
                private val _context: IrPluginContext,
                symbol: IrClassSymbol
            ) : ResolvedClass(symbol, fqName) {
                /**
                 * Get the class's type.
                 */
                public val type: IrSimpleType = owner.typeWith()

                public constructor(context: IrPluginContext) : this(context, resolveSymbol(context))

                public fun ctor(): ctor = ctor(_context)

                public fun fromString(): fromString = fromString(_context)

                public fun NestedClass(): NestedClass = NestedClass(_context)

                public fun n(): n = n(_context)

                /**
                 * Resolved reference to `tester.second.TestClass.<init>`
                 *
                 * Constructs class `tester.second.TestClass`
                 *
                 * Value parameters:
                 * * `n: kotlin.Int`
                 *
                 */
                public class ctor private constructor(
                    private val _context: IrPluginContext,
                    symbol: IrConstructorSymbol
                ) : ResolvedConstructor(symbol, fqName) {
                    /**
                     * Get the constructed type.
                     */
                    public val constructedType: IrType = owner.constructedClassType

                    public constructor(context: IrPluginContext) : this(
                        context,
                        resolveSymbol(context)
                    )

                    /**
                     * Call the constructor
                     *
                     * @param n `kotlin.Int`
                     * @return `tester.second.TestClass`
                     */
                    public fun call(builder: IrBuilderWithScope, n: IrExpression): IrConstructorCall =
                        builder.irCallConstructor(this, listOf()).apply {
                            type = owner.returnType
                            putValueArgument(0, n)
                        }


                    public class Instance(
                        public val call: IrConstructorCall
                    ) {
                        init {
                            val signature = call.symbol.signature
                            val requiredSignature = ctor.signature
                            require(signature == requiredSignature) {
                                """Instance's signature $signature did not match the required signature of $requiredSignature"""
                            }
                        }

                        public val n: IrExpression?
                            get() = call.getValueArgument(0)
                    }

                    public companion object Reference :
                        ConstructorReference<ctor>(
                            FqName("tester.second.TestClass"),
                            IdSignature.PublicSignature(
                                "tester.second",
                                "TestClass.<init>", -5182794243525578284, 0
                            )
                        ) {
                        public override fun getResolvedReference(
                            context: IrPluginContext,
                            symbol: IrConstructorSymbol
                        ): ctor = ctor(context, symbol)

                        public fun instance(call: IrConstructorCall): Instance = Instance(call)

                        public fun instanceOrNull(call: IrConstructorCall): Instance? {
                            if (call.symbol.signature == ctor.signature) {
                                return Instance(call)
                            } else {
                                return null
                            }
                        }
                    }
                }

                /**
                 * Resolved reference to `tester.second.TestClass.<init>`
                 *
                 * Constructs class `tester.second.TestClass`
                 *
                 * Value parameters:
                 * * `s: kotlin.String`
                 *
                 */
                public class fromString private constructor(
                    private val _context: IrPluginContext,
                    symbol: IrConstructorSymbol
                ) : ResolvedConstructor(symbol, fqName) {
                    /**
                     * Get the constructed type.
                     */
                    public val constructedType: IrType = owner.constructedClassType

                    public constructor(context: IrPluginContext) : this(
                        context,
                        resolveSymbol(context)
                    )

                    /**
                     * Call the constructor
                     *
                     * @param s `kotlin.String`
                     * @return `tester.second.TestClass`
                     */
                    public fun call(builder: IrBuilderWithScope, s: IrExpression): IrConstructorCall =
                        builder.irCallConstructor(this, listOf()).apply {
                            type = owner.returnType
                            putValueArgument(0, s)
                        }


                    public class Instance(
                        public val call: IrConstructorCall
                    ) {
                        init {
                            val signature = call.symbol.signature
                            val requiredSignature = fromString.signature
                            require(signature == requiredSignature) {
                                """Instance's signature $signature did not match the required signature of $requiredSignature"""
                            }
                        }

                        public val s: IrExpression?
                            get() = call.getValueArgument(0)
                    }

                    public companion object Reference :
                        ConstructorReference<fromString>(
                            FqName("tester.second.TestClass"),
                            IdSignature.PublicSignature(
                                "tester.second", "TestClass.<init>",
                                1280618353163213788, 0
                            )
                        ) {
                        public override fun getResolvedReference(
                            context: IrPluginContext,
                            symbol: IrConstructorSymbol
                        ): fromString = fromString(
                            context,
                            symbol
                        )

                        public fun instance(call: IrConstructorCall): Instance = Instance(call)

                        public fun instanceOrNull(call: IrConstructorCall): Instance? {
                            if (call.symbol.signature == fromString.signature) {
                                return Instance(call)
                            } else {
                                return null
                            }
                        }
                    }
                }

                /**
                 * Resolved reference to `tester.second.TestClass.NestedClass`
                 *
                 */
                public class NestedClass private constructor(
                    private val _context: IrPluginContext,
                    symbol: IrClassSymbol
                ) : ResolvedClass(symbol, fqName) {
                    /**
                     * Get the class's type.
                     */
                    public val type: IrSimpleType = owner.typeWith()

                    public constructor(context: IrPluginContext) : this(
                        context,
                        resolveSymbol(context)
                    )

                    public companion object Reference :
                        ClassReference<NestedClass>(
                            FqName("tester.second.TestClass.NestedClass"),
                            IdSignature.PublicSignature(
                                "tester.second", "TestClass.NestedClass",
                                null, 0
                            )
                        ) {
                        public override fun getResolvedReference(
                            context: IrPluginContext,
                            symbol: IrClassSymbol
                        ): NestedClass = NestedClass(context, symbol)
                    }
                }

                /**
                 * Resolved reference to `tester.second.TestClass.n`
                 *
                 * Dispatch receiver: `tester.second.TestClass`
                 *
                 * Type: `kotlin.Int`
                 */
                public class n private constructor(
                    private val _context: IrPluginContext,
                    symbol: IrPropertySymbol
                ) : ResolvedProperty(symbol, fqName) {
                    /**
                     * Get the property's type.
                     */
                    public val type: IrType = (owner.getter?.returnType ?: owner.backingField?.type)!!

                    /**
                     * The getter
                     */
                    public val getter: IrSimpleFunctionSymbol = owner.getter!!.symbol

                    /**
                     * The setter
                     */
                    public val setter: IrSimpleFunctionSymbol = owner.setter!!.symbol

                    /**
                     * The backing field
                     */
                    public val backingField: IrFieldSymbol = owner.backingField!!.symbol

                    public constructor(context: IrPluginContext) : this(
                        context,
                        resolveSymbol(context)
                    )

                    /**
                     * Call the getter
                     *
                     * @param dispatchReceiver `tester.second.TestClass`
                     * @return `kotlin.Int`
                     */
                    public fun `get`(builder: IrBuilderWithScope, dispatchReceiver: IrExpression):
                            IrCall = builder.irCall(getter).apply {
                        this.dispatchReceiver = dispatchReceiver
                        type = getter.owner.returnType
                    }


                    /**
                     * Call the setter
                     *
                     * @param dispatchReceiver `tester.second.TestClass`
                     * @param value `kotlin.Int`
                     * @return `kotlin.Unit`
                     */
                    public fun `set`(
                        builder: IrBuilderWithScope,
                        dispatchReceiver: IrExpression,
                        `value`: IrExpression
                    ): IrCall = builder.irCall(setter).apply {
                        this.dispatchReceiver = dispatchReceiver
                        type = setter.owner.returnType
                        putValueArgument(0, value)
                    }


                    public open class Instance(
                        public val call: IrCall
                    ) {
                        init {
                            val signature = call.symbol.owner.correspondingPropertySymbol?.signature
                            val requiredSignature = n.signature
                            require(signature == requiredSignature) {
                                """Instance's signature $signature did not match the required signature of $requiredSignature"""
                            }
                        }

                        public val dispatchReceiver: IrExpression?
                            get() = call.dispatchReceiver

                        public val `property`: IrProperty
                            get() = call.symbol.owner.correspondingPropertySymbol!!.owner
                    }

                    public class GetterInstance(
                        call: IrCall
                    ) : Instance(call) {
                        init {
                            require(call.symbol.owner.isGetter) {
                                """Instance ${call.symbol} is not the right type of accessor, expected a getter"""
                            }
                        }
                    }

                    public class SetterInstance(
                        call: IrCall
                    ) : Instance(call) {
                        init {
                            require(call.symbol.owner.isSetter) {
                                """Instance ${call.symbol} is not the right type of accessor, expected a setter"""
                            }
                        }

                        public val `value`: IrExpression?
                            get() = call.getValueArgument(0)
                    }

                    public companion object Reference :
                        PropertyReference<n>(
                            FqName("tester.second.TestClass.n"),
                            IdSignature.PublicSignature(
                                "tester.second",
                                "TestClass.n", -1061662854873377138, 0
                            )
                        ) {
                        public override fun getResolvedReference(
                            context: IrPluginContext,
                            symbol: IrPropertySymbol
                        ): n = n(context, symbol)

                        public fun accessorInstance(call: IrCall): Instance = Instance(call)

                        public fun accessorInstanceOrNull(call: IrCall): Instance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                n.signature
                            ) {
                                return Instance(call)
                            } else {
                                return null
                            }
                        }

                        public fun getterInstance(call: IrCall): GetterInstance =
                            GetterInstance(call)

                        public fun getterInstanceOrNull(call: IrCall): GetterInstance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                n.signature && call.symbol.owner.isGetter
                            ) {
                                return GetterInstance(call)
                            } else {
                                return null
                            }
                        }

                        public fun setterInstance(call: IrCall): SetterInstance =
                            SetterInstance(call)

                        public fun setterInstanceOrNull(call: IrCall): SetterInstance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                n.signature && call.symbol.owner.isSetter
                            ) {
                                return SetterInstance(call)
                            } else {
                                return null
                            }
                        }
                    }
                }

                public companion object Reference :
                    ClassReference<TestClass>(
                        FqName("tester.second.TestClass"),
                        IdSignature.PublicSignature("tester.second", "TestClass", null, 0)
                    ) {
                    public val ctor: ctor.Reference = TestClass.ctor.Reference

                    public val fromString: fromString.Reference = TestClass.fromString.Reference

                    public val NestedClass: NestedClass.Reference = TestClass.NestedClass.Reference

                    public val n: n.Reference = TestClass.n.Reference

                    public override fun getResolvedReference(
                        context: IrPluginContext,
                        symbol: IrClassSymbol
                    ): TestClass = TestClass(context, symbol)
                }
            }

            /**
             * Resolved reference to `tester.second.TestEnum`
             *
             * Enum entries
             * * [Instance.One]
             * * [Instance.Two]
             * * [Instance.Three]
             *
             */
            public class TestEnum private constructor(
                private val _context: IrPluginContext,
                symbol: IrClassSymbol
            ) : ResolvedClass(symbol, fqName) {
                /**
                 * Get the class's type.
                 */
                public val type: IrSimpleType = owner.typeWith()

                public val One: ResolvedEnumEntry<Instance> = Instance.One.reference(_context)

                public val Two: ResolvedEnumEntry<Instance> = Instance.Two.reference(_context)

                public val Three: ResolvedEnumEntry<Instance> = Instance.Three.reference(_context)

                public constructor(context: IrPluginContext) : this(context, resolveSymbol(context))

                public fun testProp(): testProp = testProp(_context)

                public enum class Instance(
                    public override val reference: EnumEntryReference<Instance>
                ) : EnumInstance {
                    One(EnumEntryReference<Instance>(TestEnum.fqName, {
                        Instance.One
                    }, IdSignature.PublicSignature("tester.second", "TestEnum.One", null, 0))),
                    Two(EnumEntryReference<Instance>(TestEnum.fqName, {
                        Instance.Two
                    }, IdSignature.PublicSignature("tester.second", "TestEnum.Two", null, 0))),
                    Three(EnumEntryReference<Instance>(TestEnum.fqName, {
                        Instance.Three
                    }, IdSignature.PublicSignature("tester.second", "TestEnum.Three", null, 0))),
                    ;
                }

                /**
                 * Resolved reference to `tester.second.TestEnum.testProp`
                 *
                 * Dispatch receiver: `tester.second.TestEnum`
                 *
                 * Type: `kotlin.String`
                 */
                public class testProp private constructor(
                    private val _context: IrPluginContext,
                    symbol: IrPropertySymbol
                ) : ResolvedProperty(symbol, fqName) {
                    /**
                     * Get the property's type.
                     */
                    public val type: IrType = (owner.getter?.returnType ?: owner.backingField?.type)!!

                    /**
                     * The getter
                     */
                    public val getter: IrSimpleFunctionSymbol = owner.getter!!.symbol

                    /**
                     * The backing field
                     */
                    public val backingField: IrFieldSymbol = owner.backingField!!.symbol

                    public constructor(context: IrPluginContext) : this(
                        context,
                        resolveSymbol(context)
                    )

                    /**
                     * Call the getter
                     *
                     * @param dispatchReceiver `tester.second.TestEnum`
                     * @return `kotlin.String`
                     */
                    public fun `get`(builder: IrBuilderWithScope, dispatchReceiver: IrExpression):
                            IrCall = builder.irCall(getter).apply {
                        this.dispatchReceiver = dispatchReceiver
                        type = getter.owner.returnType
                    }


                    public open class Instance(
                        public val call: IrCall
                    ) {
                        init {
                            val signature = call.symbol.owner.correspondingPropertySymbol?.signature
                            val requiredSignature = testProp.signature
                            require(signature == requiredSignature) {
                                """Instance's signature $signature did not match the required signature of $requiredSignature"""
                            }
                        }

                        public val dispatchReceiver: IrExpression?
                            get() = call.dispatchReceiver

                        public val `property`: IrProperty
                            get() = call.symbol.owner.correspondingPropertySymbol!!.owner
                    }

                    public class GetterInstance(
                        call: IrCall
                    ) : Instance(call) {
                        init {
                            require(call.symbol.owner.isGetter) {
                                """Instance ${call.symbol} is not the right type of accessor, expected a getter"""
                            }
                        }
                    }

                    public companion object Reference :
                        PropertyReference<testProp>(
                            FqName("tester.second.TestEnum.testProp"),
                            IdSignature.PublicSignature(
                                "tester.second", "TestEnum.testProp",
                                4463768917101701410, 0
                            )
                        ) {
                        public override fun getResolvedReference(
                            context: IrPluginContext,
                            symbol: IrPropertySymbol
                        ): testProp = testProp(context, symbol)

                        public fun accessorInstance(call: IrCall): Instance = Instance(call)

                        public fun accessorInstanceOrNull(call: IrCall): Instance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                testProp.signature
                            ) {
                                return Instance(call)
                            } else {
                                return null
                            }
                        }

                        public fun getterInstance(call: IrCall): GetterInstance =
                            GetterInstance(call)

                        public fun getterInstanceOrNull(call: IrCall): GetterInstance? {
                            if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                                testProp.signature && call.symbol.owner.isGetter
                            ) {
                                return GetterInstance(call)
                            } else {
                                return null
                            }
                        }
                    }
                }

                public companion object Reference :
                    ClassReference<TestEnum>(
                        FqName("tester.second.TestEnum"),
                        IdSignature.PublicSignature("tester.second", "TestEnum", null, 0)
                    ) {
                    public val testProp: testProp.Reference = TestEnum.testProp.Reference

                    public override fun getResolvedReference(
                        context: IrPluginContext,
                        symbol: IrClassSymbol
                    ): TestEnum = TestEnum(context, symbol)

                    public fun instance(symbol: IrEnumEntrySymbol): Instance {
                        require(symbol.owner.parentAsClass.hasEqualFqName(fqName)) {
                            """Enum symbol $symbol is not an entry of $fqName"""
                        }
                        return Instance.valueOf(symbol.owner.name.asString())
                    }

                    public fun instance(`get`: IrGetEnumValue): Instance = instance(get.symbol)
                }
            }

            /**
             * Resolved reference to `tester.second.testConst`
             *
             * Type: `kotlin.String`
             */
            public class testConst private constructor(
                private val _context: IrPluginContext,
                symbol: IrPropertySymbol
            ) : ResolvedProperty(symbol, fqName) {
                /**
                 * Get the property's type.
                 */
                public val type: IrType = (owner.getter?.returnType ?: owner.backingField?.type)!!

                /**
                 * The getter
                 */
                public val getter: IrSimpleFunctionSymbol = owner.getter!!.symbol

                /**
                 * The backing field
                 */
                public val backingField: IrFieldSymbol = owner.backingField!!.symbol

                public constructor(context: IrPluginContext) : this(context, resolveSymbol(context))

                /**
                 * Call the getter
                 *
                 * @return `kotlin.String`
                 */
                public fun `get`(builder: IrBuilderWithScope): IrCall =
                    builder.irCall(getter).apply {
                        type = getter.owner.returnType
                    }


                public open class Instance(
                    public val call: IrCall
                ) {
                    init {
                        val signature = call.symbol.owner.correspondingPropertySymbol?.signature
                        val requiredSignature = testConst.signature
                        require(signature == requiredSignature) {
                            """Instance's signature $signature did not match the required signature of $requiredSignature"""
                        }
                    }

                    public val `property`: IrProperty
                        get() = call.symbol.owner.correspondingPropertySymbol!!.owner
                }

                public class GetterInstance(
                    call: IrCall
                ) : Instance(call) {
                    init {
                        require(call.symbol.owner.isGetter) {
                            """Instance ${call.symbol} is not the right type of accessor, expected a getter"""
                        }
                    }
                }

                public companion object Reference :
                    PropertyReference<testConst>(
                        FqName("tester.second.testConst"),
                        IdSignature.PublicSignature(
                            "tester.second",
                            "testConst", -1409218079476337607, 0
                        )
                    ) {
                    public const val `value`: String = "test"

                    public override fun getResolvedReference(
                        context: IrPluginContext,
                        symbol: IrPropertySymbol
                    ): testConst = testConst(context, symbol)

                    public fun accessorInstance(call: IrCall): Instance = Instance(call)

                    public fun accessorInstanceOrNull(call: IrCall): Instance? {
                        if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                            testConst.signature
                        ) {
                            return Instance(call)
                        } else {
                            return null
                        }
                    }

                    public fun getterInstance(call: IrCall): GetterInstance = GetterInstance(call)

                    public fun getterInstanceOrNull(call: IrCall): GetterInstance? {
                        if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                            testConst.signature && call.symbol.owner.isGetter
                        ) {
                            return GetterInstance(call)
                        } else {
                            return null
                        }
                    }
                }
            }

            public companion object Reference {
                public val ExportedAnnotation: ExportedAnnotation.Reference =
                    second.ExportedAnnotation.Reference

                public val TestAnnotation: TestAnnotation.Reference =
                    second.TestAnnotation.Reference

                public val TestClass: TestClass.Reference = second.TestClass.Reference

                public val TestEnum: TestEnum.Reference = second.TestEnum.Reference

                public val testConst: testConst.Reference = second.testConst.Reference

                public operator fun invoke(context: IrPluginContext) = second(context)
            }
        }

        public companion object Reference {
            public val second: second.Reference = tester.second.Reference

            public operator fun invoke(context: IrPluginContext) = tester(context)
        }
    }

    public companion object Reference {
        public val tester: tester.Reference = Names.tester.Reference

        public operator fun invoke(context: IrPluginContext) = Names(context)
    }
}
