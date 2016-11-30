package $package$

import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import $package$.TestSpec

class $className$Test extends TestSpec {
  def with$className$(f: $className => Unit): Unit = {
    val repo = new $className$()
    f(repo)
  }

  it should "find $entityName$" in with$className$ { repo =>

  }

  it should "delete $entityName$" in with$className$ { repo =>

  }

  it should "take $entityName$" in with$className$ { repo =>

  }

  it should "add $entityName$" in with$className$ { repo =>

  }
}