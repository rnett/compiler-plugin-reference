// GENERATED, DO NOT EDIT
package test.generation

import com.rnett.plugin.ClassReference
import com.rnett.plugin.ConstructorReference
import com.rnett.plugin.FunctionReference
import com.rnett.plugin.PropertyReference
import com.rnett.plugin.TypealiasReference
import org.jetbrains.kotlin.ir.util.IdSignature
import org.jetbrains.kotlin.name.FqName

public object Names {
    public object tester {
        public val TestTypealias: TypealiasReference =
                TypealiasReference(FqName("tester.TestTypealias"),
                IdSignature.PublicSignature("tester", "TestTypealias", null, 0))

        public val testPublishedApi: FunctionReference =
                FunctionReference(FqName("tester.testPublishedApi"),
                IdSignature.PublicSignature("tester", "testPublishedApi", 5159344213406223961, 0))

        public val testPublishedApi_1: FunctionReference =
                FunctionReference(FqName("tester.testPublishedApi"),
                IdSignature.PublicSignature("tester", "testPublishedApi", 1731966692884449248, 0))

        public val testTopLevelFunction: FunctionReference =
                FunctionReference(FqName("tester.testTopLevelFunction"),
                IdSignature.PublicSignature("tester", "testTopLevelFunction", -5640281933810690202,
                0))

        public object TestClass : ClassReference(FqName("tester.TestClass"),
                IdSignature.PublicSignature("tester", "TestClass", null, 0)) {
            public val ctor: ConstructorReference = ConstructorReference(FqName("tester.TestClass"),
                    IdSignature.PublicSignature("tester", "TestClass.<init>", -5182794243525578284,
                    0))

            public val fromString: ConstructorReference =
                    ConstructorReference(FqName("tester.TestClass"),
                    IdSignature.PublicSignature("tester", "TestClass.<init>", 1280618353163213788,
                    0))

            public val NestedClass: ClassReference =
                    ClassReference(FqName("tester.TestClass.NestedClass"),
                    IdSignature.PublicSignature("tester", "TestClass.NestedClass", null, 0))

            public val n: PropertyReference = PropertyReference(FqName("tester.TestClass.n"),
                    IdSignature.PublicSignature("tester", "TestClass.n", -1061662854873377138, 0))
        }
    }
}

public object ResolvedNames
