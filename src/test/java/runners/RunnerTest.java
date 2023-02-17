package runners;

import org.junit.runner.RunWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import service.*;
import util.ValidaCpfTest;


@RunWith(JUnitPlatform.class)
@SelectClasses({CartaoDeCreditoServiceImplTest.class, CartaoDeCreditoServiceImplTest.class, ValidaCpfTest.class,
        CartaoDeCreditoServiceImplMockTest.class, ContaServiceImplMockTest.class})
public class RunnerTest {
    // mvn jacoco:report
}
