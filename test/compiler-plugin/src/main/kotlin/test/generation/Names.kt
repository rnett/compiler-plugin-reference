// GENERATED, DO NOT EDIT
package test.generation

import com.rnett.plugin.ClassReference
import com.rnett.plugin.ConstructorReference
import com.rnett.plugin.FunctionReference
import com.rnett.plugin.PropertyReference
import com.rnett.plugin.ResolvedClass
import com.rnett.plugin.ResolvedConstructor
import com.rnett.plugin.ResolvedFunction
import com.rnett.plugin.ResolvedProperty
import com.rnett.plugin.ResolvedTypealias
import com.rnett.plugin.TypealiasReference
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irCallConstructor
import org.jetbrains.kotlin.ir.builders.irVararg
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol
import org.jetbrains.kotlin.ir.symbols.IrFieldSymbol
import org.jetbrains.kotlin.ir.symbols.IrPropertySymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.IrTypeArgument
import org.jetbrains.kotlin.ir.types.impl.IrStarProjectionImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.types.typeWithArguments
import org.jetbrains.kotlin.ir.util.IdSignature
import org.jetbrains.kotlin.ir.util.constructedClass
import org.jetbrains.kotlin.ir.util.constructedClassType
import org.jetbrains.kotlin.ir.util.isGetter
import org.jetbrains.kotlin.ir.util.isSetter
import org.jetbrains.kotlin.ir.util.substitute
import org.jetbrains.kotlin.ir.util.typeSubstitutionMap
import org.jetbrains.kotlin.name.FqName

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
            public fun TestClass(): TestClass = TestClass(_context)

            public fun TestTypealias(): TestTypealias = TestTypealias(_context)

            public fun TestTypealiasWithArg(): TestTypealiasWithArg = TestTypealiasWithArg(_context)

            public fun WithTypeParams(): WithTypeParams = WithTypeParams(_context)

            public fun testPropWithTypeVar(): testPropWithTypeVar = testPropWithTypeVar(_context)

            public fun testPublishedApi(): testPublishedApi = testPublishedApi(_context)

            public fun testPublishedApi_1(): testPublishedApi_1 = testPublishedApi_1(_context)

            public fun testTopLevelFunction(): testTopLevelFunction = testTopLevelFunction(_context)

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
             * Resolved reference to `tester.second.TestTypealias`
             *
             */
            public class TestTypealias private constructor(
                private val _context: IrPluginContext,
                symbol: IrTypeAliasSymbol
            ) : ResolvedTypealias(symbol, fqName) {
                /**
                 * Get the expanded type.
                 */
                public val type: IrType = owner.expandedType

                public constructor(context: IrPluginContext) : this(context, resolveSymbol(context))

                public class Instance

                public companion object Reference :
                    TypealiasReference<TestTypealias>(
                        FqName("tester.second.TestTypealias"),
                        IdSignature.PublicSignature("tester.second", "TestTypealias", null, 0)
                    ) {
                    public override fun getResolvedReference(
                        context: IrPluginContext,
                        symbol: IrTypeAliasSymbol
                    ): TestTypealias = TestTypealias(
                        context,
                        symbol
                    )
                }
            }

            /**
             * Resolved reference to `tester.second.TestTypealiasWithArg`
             *
             * Type parameters:
             * * `T`
             *
             */
            public class TestTypealiasWithArg private constructor(
                private val _context: IrPluginContext,
                symbol: IrTypeAliasSymbol
            ) : ResolvedTypealias(symbol, fqName) {
                public constructor(context: IrPluginContext) : this(context, resolveSymbol(context))

                /**
                 * Get the expanded type.
                 *
                 * @param T `?`
                 */
                public fun type(T: IrType): IrType =
                    owner.expandedType.substitute(owner.typeParameters, listOf(T))

                public class Instance

                public companion object Reference :
                    TypealiasReference<TestTypealiasWithArg>(
                        FqName("tester.second.TestTypealiasWithArg"),
                        IdSignature.PublicSignature(
                            "tester.second", "TestTypealiasWithArg", null,
                            0
                        )
                    ) {
                    public override fun getResolvedReference(
                        context: IrPluginContext,
                        symbol: IrTypeAliasSymbol
                    ): TestTypealiasWithArg =
                        TestTypealiasWithArg(context, symbol)
                }
            }

            /**
             * Resolved reference to `tester.second.WithTypeParams`
             *
             * Type parameters:
             * * `T : kotlin.Number`
             *
             */
            public class WithTypeParams private constructor(
                private val _context: IrPluginContext,
                symbol: IrClassSymbol
            ) : ResolvedClass(symbol, fqName) {
                public constructor(context: IrPluginContext) : this(context, resolveSymbol(context))

                /**
                 * Get the class's type.
                 *
                 * @param T `? : kotlin.Number`
                 */
                public fun type(T: IrType): IrSimpleType = owner.typeWith(T)

                /**
                 * Get the class's type.
                 *
                 * @param T `? : kotlin.Number`
                 */
                public fun type(T: IrTypeArgument = IrStarProjectionImpl): IrSimpleType =
                    typeWithArguments(listOf(T));

                public fun ctor(): ctor = ctor(_context)

                /**
                 * Resolved reference to `tester.second.WithTypeParams.<init>`
                 *
                 * Constructs class `tester.second.WithTypeParams<T of
                 * tester.second.WithTypeParams>`
                 *
                 * Class type parameters:
                 * * `T : kotlin.Number`
                 *
                 * Value parameters:
                 * * `n: T of tester.second.WithTypeParams`
                 *
                 */
                public class ctor private constructor(
                    private val _context: IrPluginContext,
                    symbol: IrConstructorSymbol
                ) : ResolvedConstructor(symbol, fqName) {
                    public constructor(context: IrPluginContext) : this(
                        context,
                        resolveSymbol(context)
                    )

                    /**
                     * Get the constructed type.
                     *
                     * @param T `? : kotlin.Number`
                     */
                    public fun constructedType(T: IrType): IrType =
                        owner.constructedClassType.substitute(
                            owner.constructedClass.typeParameters,
                            listOf(T)
                        )

                    /**
                     * Call the constructor
                     *
                     * @param T `? : kotlin.Number`
                     * @param n `T of tester.second.WithTypeParams`
                     * @return `tester.second.WithTypeParams<T of tester.second.WithTypeParams>`
                     */
                    public fun call(
                        builder: IrBuilderWithScope,
                        T: IrType,
                        n: IrExpression
                    ): IrConstructorCall = builder.irCallConstructor(this, listOf(T)).apply {
                        type = owner.returnType.substitute(typeSubstitutionMap)
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

                        public val T: IrType?
                            get() = call.getTypeArgument(0)

                        public val n: IrExpression?
                            get() = call.getValueArgument(0)
                    }

                    public companion object Reference :
                        ConstructorReference<ctor>(
                            FqName("tester.second.WithTypeParams"),
                            IdSignature.PublicSignature(
                                "tester.second",
                                "WithTypeParams.<init>", -8731461708390519279, 0
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

                public companion object Reference :
                    ClassReference<WithTypeParams>(
                        FqName("tester.second.WithTypeParams"),
                        IdSignature.PublicSignature("tester.second", "WithTypeParams", null, 0)
                    ) {
                    public val ctor: ctor.Reference = WithTypeParams.ctor.Reference

                    public override fun getResolvedReference(
                        context: IrPluginContext,
                        symbol: IrClassSymbol
                    ): WithTypeParams = WithTypeParams(context, symbol)
                }
            }

            /**
             * Resolved reference to `tester.second.testPropWithTypeVar`
             *
             * Type parameters:
             * * `T : kotlin.Number`
             *
             * Extension receiver: `T of tester.second.<get-testPropWithTypeVar>`
             *
             * Type: `kotlin.Int`
             */
            public class testPropWithTypeVar private constructor(
                private val _context: IrPluginContext,
                symbol: IrPropertySymbol
            ) : ResolvedProperty(symbol, fqName) {
                /**
                 * The getter
                 */
                public val getter: IrSimpleFunctionSymbol = owner.getter!!.symbol

                public constructor(context: IrPluginContext) : this(context, resolveSymbol(context))

                /**
                 * Get the property's type.
                 *
                 * @param T `? : kotlin.Number`
                 */
                public fun type(T: IrType): IrType = (owner.getter?.returnType ?: owner.backingField?.type)!!.substitute(
                    owner.getter!!.typeParameters,
                    listOf(T)
                )

                /**
                 * Call the getter
                 *
                 * @param T `? : kotlin.Number`
                 * @param extensionReceiver `T of tester.second.<get-testPropWithTypeVar>`
                 * @return `kotlin.Int`
                 */
                public fun `get`(
                    builder: IrBuilderWithScope,
                    T: IrType,
                    extensionReceiver: IrExpression
                ): IrCall = builder.irCall(getter).apply {
                    putTypeArgument(0, T)
                    this.extensionReceiver = extensionReceiver
                    type = getter.owner.returnType.substitute(typeSubstitutionMap)
                }


                public open class Instance(
                    public val call: IrCall
                ) {
                    init {
                        val signature = call.symbol.owner.correspondingPropertySymbol?.signature
                        val requiredSignature = testPropWithTypeVar.signature
                        require(signature == requiredSignature) {
                            """Instance's signature $signature did not match the required signature of $requiredSignature"""
                        }
                    }

                    public val T: IrType?
                        get() = call.getTypeArgument(0)

                    public val extensionReceiver: IrExpression?
                        get() = call.extensionReceiver

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
                    PropertyReference<testPropWithTypeVar>(
                        FqName("tester.second.testPropWithTypeVar"),
                        IdSignature.PublicSignature(
                            "tester.second",
                            "testPropWithTypeVar", -2301283503845253114, 0
                        )
                    ) {
                    public override fun getResolvedReference(
                        context: IrPluginContext,
                        symbol: IrPropertySymbol
                    ): testPropWithTypeVar =
                        testPropWithTypeVar(context, symbol)

                    public fun accessorInstance(call: IrCall): Instance = Instance(call)

                    public fun accessorInstanceOrNull(call: IrCall): Instance? {
                        if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                            testPropWithTypeVar.signature
                        ) {
                            return Instance(call)
                        } else {
                            return null
                        }
                    }

                    public fun getterInstance(call: IrCall): GetterInstance = GetterInstance(call)

                    public fun getterInstanceOrNull(call: IrCall): GetterInstance? {
                        if (call.symbol.owner.correspondingPropertySymbol?.signature ==
                            testPropWithTypeVar.signature && call.symbol.owner.isGetter
                        ) {
                            return GetterInstance(call)
                        } else {
                            return null
                        }
                    }
                }
            }

            /**
             * Resolved reference to `tester.second.testPublishedApi`
             *
             * Return type: `kotlin.Unit`
             */
            public class testPublishedApi private constructor(
                private val _context: IrPluginContext,
                symbol: IrSimpleFunctionSymbol
            ) : ResolvedFunction(symbol, fqName) {
                /**
                 * Get the return type.
                 */
                public val returnType: IrType = owner.returnType

                public constructor(context: IrPluginContext) : this(context, resolveSymbol(context))

                /**
                 * Call the function
                 *
                 * @return `kotlin.Unit`
                 */
                public fun call(builder: IrBuilderWithScope): IrCall = builder.irCall(this).apply {
                    type = owner.returnType
                }


                public class Instance(
                    public val call: IrCall
                ) {
                    init {
                        val signature = call.symbol.signature
                        val requiredSignature = testPublishedApi.signature
                        require(signature == requiredSignature) {
                            """Instance's signature $signature did not match the required signature of $requiredSignature"""
                        }
                    }
                }

                public companion object Reference :
                    FunctionReference<testPublishedApi>(
                        FqName("tester.second.testPublishedApi"),
                        IdSignature.PublicSignature(
                            "tester.second", "testPublishedApi",
                            5159344213406223961, 0
                        )
                    ) {
                    public override fun getResolvedReference(
                        context: IrPluginContext,
                        symbol: IrSimpleFunctionSymbol
                    ): testPublishedApi =
                        testPublishedApi(context, symbol)

                    public fun instance(call: IrCall): Instance = Instance(call)

                    public fun instanceOrNull(call: IrCall): Instance? {
                        if (call.symbol.signature == testPublishedApi.signature) {
                            return Instance(call)
                        } else {
                            return null
                        }
                    }
                }
            }

            /**
             * Resolved reference to `tester.second.testPublishedApi`
             *
             * Value parameters:
             * * `t: kotlin.Int`
             *
             * Return type: `kotlin.Unit`
             */
            public class testPublishedApi_1 private constructor(
                private val _context: IrPluginContext,
                symbol: IrSimpleFunctionSymbol
            ) : ResolvedFunction(symbol, fqName) {
                /**
                 * Get the return type.
                 */
                public val returnType: IrType = owner.returnType

                public constructor(context: IrPluginContext) : this(context, resolveSymbol(context))

                /**
                 * Call the function
                 *
                 * @param t `kotlin.Int`
                 * @return `kotlin.Unit`
                 */
                public fun call(builder: IrBuilderWithScope, t: IrExpression): IrCall =
                    builder.irCall(this).apply {
                        type = owner.returnType
                        putValueArgument(0, t)
                    }


                public class Instance(
                    public val call: IrCall
                ) {
                    init {
                        val signature = call.symbol.signature
                        val requiredSignature = testPublishedApi_1.signature
                        require(signature == requiredSignature) {
                            """Instance's signature $signature did not match the required signature of $requiredSignature"""
                        }
                    }

                    public val t: IrExpression?
                        get() = call.getValueArgument(0)
                }

                public companion object Reference :
                    FunctionReference<testPublishedApi_1>(
                        FqName("tester.second.testPublishedApi"),
                        IdSignature.PublicSignature(
                            "tester.second", "testPublishedApi",
                            1731966692884449248, 0
                        )
                    ) {
                    public override fun getResolvedReference(
                        context: IrPluginContext,
                        symbol: IrSimpleFunctionSymbol
                    ): testPublishedApi_1 =
                        testPublishedApi_1(context, symbol)

                    public fun instance(call: IrCall): Instance = Instance(call)

                    public fun instanceOrNull(call: IrCall): Instance? {
                        if (call.symbol.signature == testPublishedApi_1.signature) {
                            return Instance(call)
                        } else {
                            return null
                        }
                    }
                }
            }

            /**
             * Resolved reference to `tester.second.testTopLevelFunction`
             *
             * Type parameters:
             * * `T`
             *
             * Extension receiver: `kotlin.Int`
             *
             * Value parameters:
             * * `req: T of tester.second.testTopLevelFunction`
             * * `opt: kotlin.Double? = ...`
             * * `vararg t: kotlin.Array<out kotlin.String>`
             *
             * Return type: `kotlin.Int`
             */
            public class testTopLevelFunction private constructor(
                private val _context: IrPluginContext,
                symbol: IrSimpleFunctionSymbol
            ) : ResolvedFunction(symbol, fqName) {
                public constructor(context: IrPluginContext) : this(context, resolveSymbol(context))

                /**
                 * Get the return type.
                 *
                 * @param T `?`
                 */
                public fun returnType(T: IrType): IrType =
                    owner.returnType.substitute(owner.typeParameters, listOf(T))

                /**
                 * Call the function
                 *
                 * @param T `?`
                 * @param extensionReceiver `kotlin.Int`
                 * @param req `T of tester.second.testTopLevelFunction`
                 * @param opt `kotlin.Double? = ...`
                 * @param t `vararg kotlin.Array<out kotlin.String>`
                 * @return `kotlin.Int`
                 */
                public fun call(
                    builder: IrBuilderWithScope,
                    T: IrType,
                    extensionReceiver: IrExpression,
                    req: IrExpression,
                    opt: IrExpression? = null,
                    t: IrExpression
                ): IrCall = builder.irCall(this).apply {
                    putTypeArgument(0, T)
                    this.extensionReceiver = extensionReceiver
                    type = owner.returnType.substitute(typeSubstitutionMap)
                    putValueArgument(0, req)
                    if (opt != null) {
                        putValueArgument(1, opt)
                    }
                    putValueArgument(2, t)
                }


                /**
                 * Call the function
                 *
                 * @param T `?`
                 * @param extensionReceiver `kotlin.Int`
                 * @param req `T of tester.second.testTopLevelFunction`
                 * @param opt `kotlin.Double? = ...`
                 * @param t `vararg kotlin.Array<out kotlin.String>`
                 * @return `kotlin.Int`
                 */
                public fun callVararg(
                    builder: IrBuilderWithScope,
                    T: IrType,
                    extensionReceiver: IrExpression,
                    req: IrExpression,
                    opt: IrExpression? = null,
                    t: Iterable<IrExpression>
                ): IrCall = builder.irCall(this).apply {
                    putTypeArgument(0, T)
                    this.extensionReceiver = extensionReceiver
                    type = owner.returnType.substitute(typeSubstitutionMap)
                    putValueArgument(0, req)
                    if (opt != null) {
                        putValueArgument(1, opt)
                    }
                    putValueArgument(
                        2,
                        builder.irVararg(
                            owner.valueParameters[2].varargElementType!!.substitute(typeSubstitutionMap),
                            t.toList()
                        )
                    )
                }


                public class Instance(
                    public val call: IrCall
                ) {
                    init {
                        val signature = call.symbol.signature
                        val requiredSignature = testTopLevelFunction.signature
                        require(signature == requiredSignature) {
                            """Instance's signature $signature did not match the required signature of $requiredSignature"""
                        }
                    }

                    public val T: IrType?
                        get() = call.getTypeArgument(0)

                    public val req: IrExpression?
                        get() = call.getValueArgument(0)

                    public val opt: IrExpression?
                        get() = call.getValueArgument(1)

                    public val t: IrExpression?
                        get() = call.getValueArgument(2)

                    public val extensionReceiver: IrExpression?
                        get() = call.extensionReceiver
                }

                public companion object Reference :
                    FunctionReference<testTopLevelFunction>(
                        FqName("tester.second.testTopLevelFunction"),
                        IdSignature.PublicSignature(
                            "tester.second",
                            "testTopLevelFunction", -3918369520275516103, 0
                        )
                    ) {
                    public override fun getResolvedReference(
                        context: IrPluginContext,
                        symbol: IrSimpleFunctionSymbol
                    ): testTopLevelFunction =
                        testTopLevelFunction(context, symbol)

                    public fun instance(call: IrCall): Instance = Instance(call)

                    public fun instanceOrNull(call: IrCall): Instance? {
                        if (call.symbol.signature == testTopLevelFunction.signature) {
                            return Instance(call)
                        } else {
                            return null
                        }
                    }
                }
            }

            public companion object Reference {
                public val TestClass: TestClass.Reference = second.TestClass.Reference

                public val TestTypealias: TestTypealias.Reference = second.TestTypealias.Reference

                public val TestTypealiasWithArg: TestTypealiasWithArg.Reference =
                    second.TestTypealiasWithArg.Reference

                public val WithTypeParams: WithTypeParams.Reference =
                    second.WithTypeParams.Reference

                public val testPropWithTypeVar: testPropWithTypeVar.Reference =
                    second.testPropWithTypeVar.Reference

                public val testPublishedApi: testPublishedApi.Reference =
                    second.testPublishedApi.Reference

                public val testPublishedApi_1: testPublishedApi_1.Reference =
                    second.testPublishedApi_1.Reference

                public val testTopLevelFunction: testTopLevelFunction.Reference =
                    second.testTopLevelFunction.Reference

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
