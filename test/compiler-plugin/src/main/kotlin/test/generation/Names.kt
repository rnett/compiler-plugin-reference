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
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.IrTypeArgument
import org.jetbrains.kotlin.ir.types.impl.IrStarProjectionImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.types.typeWithArguments
import org.jetbrains.kotlin.ir.util.IdSignature
import org.jetbrains.kotlin.ir.util.substitute
import org.jetbrains.kotlin.ir.util.typeSubstitutionMap
import org.jetbrains.kotlin.name.FqName

public object Names {
    public object tester {
        public val TestTypealias: TypealiasReference =
            TypealiasReference(
                FqName("tester.TestTypealias"),
                IdSignature.PublicSignature("tester", "TestTypealias", null, 0)
            )

        public val testPublishedApi: FunctionReference =
            FunctionReference(
                FqName("tester.testPublishedApi"),
                IdSignature.PublicSignature("tester", "testPublishedApi", 5159344213406223961, 0)
            )

        public val testPublishedApi_1: FunctionReference =
            FunctionReference(
                FqName("tester.testPublishedApi"),
                IdSignature.PublicSignature("tester", "testPublishedApi", 1731966692884449248, 0)
            )

        public val testTopLevelFunction: FunctionReference =
            FunctionReference(
                FqName("tester.testTopLevelFunction"),
                IdSignature.PublicSignature(
                    "tester", "testTopLevelFunction", -3918369520275516103,
                    0
                )
            )

        public object TestClass : ClassReference(
            FqName("tester.TestClass"),
            IdSignature.PublicSignature("tester", "TestClass", null, 0)
        ) {
            public val ctor: ConstructorReference = ConstructorReference(
                FqName("tester.TestClass"),
                IdSignature.PublicSignature(
                    "tester", "TestClass.<init>", -5182794243525578284,
                    0
                )
            )

            public val fromString: ConstructorReference =
                ConstructorReference(
                    FqName("tester.TestClass"),
                    IdSignature.PublicSignature(
                        "tester", "TestClass.<init>", 1280618353163213788,
                        0
                    )
                )

            public val NestedClass: ClassReference =
                ClassReference(
                    FqName("tester.TestClass.NestedClass"),
                    IdSignature.PublicSignature("tester", "TestClass.NestedClass", null, 0)
                )

            public val n: PropertyReference = PropertyReference(
                FqName("tester.TestClass.n"),
                IdSignature.PublicSignature("tester", "TestClass.n", -1061662854873377138, 0)
            )
        }

        public object WithTypeParams : ClassReference(
            FqName("tester.WithTypeParams"),
            IdSignature.PublicSignature("tester", "WithTypeParams", null, 0)
        ) {
            public val ctor: ConstructorReference =
                ConstructorReference(
                    FqName("tester.WithTypeParams"),
                    IdSignature.PublicSignature(
                        "tester",
                        "WithTypeParams.<init>", -8731461708390519279, 0
                    )
                )
        }
    }
}

public class ResolvedNames(
    public val context: IrPluginContext
) {
    public val tester: _tester = test.generation.ResolvedNames._tester(context)

    public class _tester(
        context: IrPluginContext
    ) {
        public val TestClass: _TestClass = test.generation.ResolvedNames._tester._TestClass(context)

        public val TestTypealias: _TestTypealias =
            test.generation.ResolvedNames._tester._TestTypealias(context)

        public val WithTypeParams: _WithTypeParams =
            test.generation.ResolvedNames._tester._WithTypeParams(context)

        public val testPublishedApi: _testPublishedApi =
            test.generation.ResolvedNames._tester._testPublishedApi(context)

        public val testPublishedApi_1: _testPublishedApi_1 =
            test.generation.ResolvedNames._tester._testPublishedApi_1(context)

        public val testTopLevelFunction: _testTopLevelFunction =
            test.generation.ResolvedNames._tester._testTopLevelFunction(context)

        /**
         * Resolved reference to `tester.TestClass`
         *
         */
        public class _TestClass(
            context: IrPluginContext
        ) : ResolvedClass(
            Names.tester.TestClass.resolveSymbol(context),
            Names.tester.TestClass.fqName
        ) {
            /**
             * Get the class's type.
             */
            public val type: IrType
                get() = owner.typeWith()

            public val ctor: _ctor = test.generation.ResolvedNames._tester._TestClass._ctor(context)

            public val fromString: _fromString =
                test.generation.ResolvedNames._tester._TestClass._fromString(context)

            public val NestedClass: _NestedClass =
                test.generation.ResolvedNames._tester._TestClass._NestedClass(context)

            public val n: _n = test.generation.ResolvedNames._tester._TestClass._n(context)

            /**
             * Resolved reference to `tester.TestClass.<init>`
             *
             * Constructs class `tester.TestClass`
             *
             * Value parameters:
             * * `n: kotlin.Int`
             *
             */
            public class _ctor(
                context: IrPluginContext
            ) : ResolvedConstructor(
                Names.tester.TestClass.ctor.resolveSymbol(context),
                Names.tester.TestClass.ctor.fqName
            ) {
                /**
                 * Call the constructor
                 *
                 * @param n `kotlin.Int`
                 * @return `tester.TestClass`
                 */
                public fun call(builder: IrBuilderWithScope, n: IrExpression): IrConstructorCall =
                    builder.irCallConstructor(this, listOf()).apply {
                        putValueArgument(0, n)
                    }

            }

            /**
             * Resolved reference to `tester.TestClass.<init>`
             *
             * Constructs class `tester.TestClass`
             *
             * Value parameters:
             * * `s: kotlin.String`
             *
             */
            public class _fromString(
                context: IrPluginContext
            ) : ResolvedConstructor(
                Names.tester.TestClass.fromString.resolveSymbol(context),
                Names.tester.TestClass.fromString.fqName
            ) {
                /**
                 * Call the constructor
                 *
                 * @param s `kotlin.String`
                 * @return `tester.TestClass`
                 */
                public fun call(builder: IrBuilderWithScope, s: IrExpression): IrConstructorCall =
                    builder.irCallConstructor(this, listOf()).apply {
                        putValueArgument(0, s)
                    }

            }

            /**
             * Resolved reference to `tester.TestClass.NestedClass`
             *
             */
            public class _NestedClass(
                context: IrPluginContext
            ) : ResolvedClass(
                Names.tester.TestClass.NestedClass.resolveSymbol(context),
                Names.tester.TestClass.NestedClass.fqName
            ) {
                /**
                 * Get the class's type.
                 */
                public val type: IrType
                    get() = owner.typeWith()
            }

            /**
             * Resolved reference to `tester.TestClass.n`
             *
             */
            public class _n(
                context: IrPluginContext
            ) : ResolvedProperty(
                Names.tester.TestClass.n.resolveSymbol(context),
                Names.tester.TestClass.n.fqName
            )
        }

        /**
         * Resolved reference to `tester.TestTypealias`
         *
         */
        public class _TestTypealias(
            context: IrPluginContext
        ) : ResolvedTypealias(
            Names.tester.TestTypealias.resolveSymbol(context),
            Names.tester.TestTypealias.fqName
        )

        /**
         * Resolved reference to `tester.WithTypeParams`
         *
         * Type parameters:
         * * `T : kotlin.Number`
         */
        public class _WithTypeParams(
            context: IrPluginContext
        ) : ResolvedClass(
            Names.tester.WithTypeParams.resolveSymbol(context),
            Names.tester.WithTypeParams.fqName
        ) {
            public val ctor: _ctor =
                test.generation.ResolvedNames._tester._WithTypeParams._ctor(context)

            /**
             * Get the class's type.
             * @param T `? : kotlin.Number`
             */
            public fun type(T: IrType): IrSimpleType = owner.typeWith(T)

            /**
             * Get the class's type.
             * @param T `? : kotlin.Number`
             */
            public fun type(T: IrTypeArgument = IrStarProjectionImpl): IrSimpleType =
                typeWithArguments(listOf(T))

            /**
             * Resolved reference to `tester.WithTypeParams.<init>`
             *
             * Constructs class `tester.WithTypeParams<T of tester.WithTypeParams>`
             *
             * Class type parameters:
             * * `T : kotlin.Number`
             *
             * Value parameters:
             * * `n: T of tester.WithTypeParams`
             *
             */
            public class _ctor(
                context: IrPluginContext
            ) : ResolvedConstructor(
                Names.tester.WithTypeParams.ctor.resolveSymbol(context),
                Names.tester.WithTypeParams.ctor.fqName
            ) {
                /**
                 * Call the constructor
                 *
                 * @param T `? : kotlin.Number`
                 * @param n `T of tester.WithTypeParams`
                 * @return `tester.WithTypeParams<T of tester.WithTypeParams>`
                 */
                public fun call(
                    builder: IrBuilderWithScope,
                    T: IrType,
                    n: IrExpression
                ): IrConstructorCall = builder.irCallConstructor(this, listOf(T)).apply {
                    putValueArgument(0, n)
                }

            }
        }

        /**
         * Resolved reference to `tester.testPublishedApi`
         *
         * Return type: `kotlin.Unit`
         */
        public class _testPublishedApi(
            context: IrPluginContext
        ) : ResolvedFunction(
            Names.tester.testPublishedApi.resolveSymbol(context),
            Names.tester.testPublishedApi.fqName
        ) {
            /**
             * Call the function
             *
             * @return `kotlin.Unit`
             */
            public fun call(builder: IrBuilderWithScope): IrCall = builder.irCall(this).apply {
                type = owner.returnType
            }

        }

        /**
         * Resolved reference to `tester.testPublishedApi`
         *
         * Value parameters:
         * * `t: kotlin.Int`
         *
         * Return type: `kotlin.Unit`
         */
        public class _testPublishedApi_1(
            context: IrPluginContext
        ) : ResolvedFunction(
            Names.tester.testPublishedApi_1.resolveSymbol(context),
            Names.tester.testPublishedApi_1.fqName
        ) {
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

        }

        /**
         * Resolved reference to `tester.testTopLevelFunction`
         *
         * Type parameters:
         * * `T : kotlin.Any?`
         *
         * Extension receiver: `kotlin.Int`
         *
         * Value parameters:
         * * `req: T of tester.testTopLevelFunction`
         * * `opt: kotlin.Double? = ...`
         * * `vararg t: kotlin.Array<out kotlin.String>`
         *
         * Return type: `kotlin.Int`
         */
        public class _testTopLevelFunction(
            context: IrPluginContext
        ) : ResolvedFunction(
            Names.tester.testTopLevelFunction.resolveSymbol(context),
            Names.tester.testTopLevelFunction.fqName
        ) {
            /**
             * Call the function
             *
             * @param T `? : kotlin.Any?`
             * @param extensionReceiver `kotlin.Int`
             * @param req `T of tester.testTopLevelFunction`
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
             * @param T `? : kotlin.Any?`
             * @param extensionReceiver `kotlin.Int`
             * @param req `T of tester.testTopLevelFunction`
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

        }
    }
}
