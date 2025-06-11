package hello.core;

@Configuration
@ComponentScan(
    basePackages = "hello.core.member",
    basePackageClasses = AutoAppConfig.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)   // @Componet가 붙은 것을 모두 등록. Configuration 클래스 제외
public class AutoAppConfig {
}