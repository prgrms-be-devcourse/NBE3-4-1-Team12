
```
backend
├─ .idea
│  ├─ backend.iml
│  ├─ compiler.xml
│  ├─ dataSources
│  ├─ dataSources.local.xml
│  ├─ dataSources.xml
│  ├─ encodings.xml
│  ├─ gradle.xml
│  ├─ jarRepositories.xml
│  ├─ misc.xml
│  ├─ modules.xml
│  ├─ uiDesigner.xml
│  ├─ vcs.xml
│  └─ workspace.xml
└─ coffeeBean_backend
   ├─ .gradle
   │  ├─ 8.5
   │  │  ├─ checksums
   │  │  │  ├─ checksums.lock
   │  │  │  ├─ md5-checksums.bin
   │  │  │  └─ sha1-checksums.bin
   │  │  ├─ dependencies-accessors
   │  │  │  ├─ dependencies-accessors.lock
   │  │  │  └─ gc.properties
   │  │  ├─ executionHistory
   │  │  │  └─ executionHistory.lock
   │  │  ├─ fileChanges
   │  │  │  └─ last-build.bin
   │  │  ├─ fileHashes
   │  │  │  └─ fileHashes.lock
   │  │  ├─ gc.properties
   │  │  └─ vcsMetadata
   │  ├─ buildOutputCleanup
   │  │  ├─ buildOutputCleanup.lock
   │  │  └─ cache.properties
   │  └─ vcs-1
   │     └─ gc.properties
   ├─ .idea
   │  ├─ gradle.xml
   │  ├─ misc.xml
   │  ├─ uiDesigner.xml
   │  ├─ vcs.xml
   │  └─ workspace.xml
   ├─ build.gradle
   ├─ gradle
   │  └─ wrapper
   │     ├─ gradle-wrapper.jar
   │     └─ gradle-wrapper.properties
   ├─ gradlew
   ├─ gradlew.bat
   ├─ out
   │  ├─ production
   │  │  ├─ classes
   │  │  │  └─ com
   │  │  │     └─ ll
   │  │  │        └─ coffeeBean
   │  │  │           ├─ domain
   │  │  │           │  ├─ coffeeBean
   │  │  │           │  │  ├─ controller
   │  │  │           │  │  │  ├─ CoffeeBeanController$UpdateCoffeeBeanRequest.class
   │  │  │           │  │  │  └─ CoffeeBeanController.class
   │  │  │           │  │  ├─ dto
   │  │  │           │  │  │  ├─ CoffeeBeanRequestDTO.class
   │  │  │           │  │  │  └─ CoffeeBeanResponseDTO.class
   │  │  │           │  │  ├─ entity
   │  │  │           │  │  │  ├─ CoffeeBean$CoffeeBeanBuilder.class
   │  │  │           │  │  │  └─ CoffeeBean.class
   │  │  │           │  │  ├─ repository
   │  │  │           │  │  │  └─ CoffeeBeanRepository.class
   │  │  │           │  │  └─ service
   │  │  │           │  │     └─ CoffeeBeanService.class
   │  │  │           │  ├─ order
   │  │  │           │  │  ├─ config
   │  │  │           │  │  │  └─ scheduler
   │  │  │           │  │  │     ├─ OrderScheduler.class
   │  │  │           │  │  │     └─ PastOrderScheduler.class
   │  │  │           │  │  ├─ controller
   │  │  │           │  │  │  └─ OrderController.class
   │  │  │           │  │  ├─ dto
   │  │  │           │  │  │  ├─ BeanNameQuantityDTO.class
   │  │  │           │  │  │  ├─ GetResDetailOrderDto.class
   │  │  │           │  │  │  ├─ GetResMenuOrderDto.class
   │  │  │           │  │  │  ├─ GetResPastOrderDto.class
   │  │  │           │  │  │  ├─ PostDetailOrderDto.class
   │  │  │           │  │  │  ├─ PostOrderRequestDto.class
   │  │  │           │  │  │  ├─ PostOrderResponseDto.class
   │  │  │           │  │  │  └─ PutMenuOrderRqDTO.class
   │  │  │           │  │  ├─ entity
   │  │  │           │  │  │  ├─ DetailOrder$DetailOrderBuilder.class
   │  │  │           │  │  │  ├─ DetailOrder.class
   │  │  │           │  │  │  ├─ MenuOrder$MenuOrderBuilder.class
   │  │  │           │  │  │  ├─ MenuOrder.class
   │  │  │           │  │  │  ├─ PastOrder$PastOrderBuilder.class
   │  │  │           │  │  │  └─ PastOrder.class
   │  │  │           │  │  ├─ enums
   │  │  │           │  │  │  └─ OrderStatus.class
   │  │  │           │  │  ├─ repository
   │  │  │           │  │  │  ├─ DetailOrderRepository.class
   │  │  │           │  │  │  ├─ OrderRepository.class
   │  │  │           │  │  │  └─ PastOrderRepository.class
   │  │  │           │  │  └─ service
   │  │  │           │  │     ├─ DetailOrderService.class
   │  │  │           │  │     ├─ OrderService.class
   │  │  │           │  │     └─ PastOrderService.class
   │  │  │           │  └─ siteUser
   │  │  │           │     ├─ controller
   │  │  │           │     │  ├─ SiteUserController$LoginDto.class
   │  │  │           │     │  └─ SiteUserController.class
   │  │  │           │     ├─ dto
   │  │  │           │     │  └─ CustomerDto.class
   │  │  │           │     ├─ entity
   │  │  │           │     │  ├─ SiteUser$SiteUserBuilder.class
   │  │  │           │     │  └─ SiteUser.class
   │  │  │           │     ├─ repository
   │  │  │           │     │  └─ SiteUserRepository.class
   │  │  │           │     └─ service
   │  │  │           │        ├─ SiteUserService.class
   │  │  │           │        └─ UserService.class
   │  │  │           ├─ global
   │  │  │           │  ├─ appConfig
   │  │  │           │  │  ├─ AppConfig.class
   │  │  │           │  │  ├─ CorsConfig$1.class
   │  │  │           │  │  └─ CorsConfig.class
   │  │  │           │  ├─ aspect
   │  │  │           │  │  └─ ResponseAspect.class
   │  │  │           │  ├─ exceptions
   │  │  │           │  │  └─ ServiceException.class
   │  │  │           │  ├─ globalExceptionHandler
   │  │  │           │  │  └─ GlobalExceptionHandler.class
   │  │  │           │  ├─ initData
   │  │  │           │  │  └─ BaseInitData.class
   │  │  │           │  ├─ jpa
   │  │  │           │  │  └─ entity
   │  │  │           │  │     ├─ BaseEntity.class
   │  │  │           │  │     └─ BaseTime.class
   │  │  │           │  └─ rsData
   │  │  │           │     └─ RsData.class
   │  │  │           ├─ standard
   │  │  │           │  ├─ base
   │  │  │           │  │  └─ Empty.class
   │  │  │           │  ├─ PageDto
   │  │  │           │  │  └─ PageDto.class
   │  │  │           │  └─ util
   │  │  │           │     ├─ Ut$json.class
   │  │  │           │     ├─ Ut$str.class
   │  │  │           │     └─ Ut.class
   │  │  │           └─ WebApplication.class
   │  │  └─ resources
   │  │     ├─ application-dev.yml
   │  │     ├─ application-test.yml
   │  │     ├─ application.yml
   │  │     └─ logback.xml
   │  └─ test
   │     └─ classes
   │        ├─ com
   │        │  └─ ll
   │        │     └─ coffeeBean
   │        │        ├─ coffeeBean
   │        │        │  └─ controller
   │        │        │     └─ CoffeeBeanControllerTest.class
   │        │        ├─ domain
   │        │        │  ├─ order
   │        │        │  │  ├─ controller
   │        │        │  │  │  └─ OrderControllerTest.class
   │        │        │  │  └─ service
   │        │        │  │     └─ OrderServiceTest.class
   │        │        │  └─ siteUser
   │        │        │     ├─ controller
   │        │        │     │  └─ SiteUserControllerTest.class
   │        │        │     └─ service
   │        │        │        └─ SiteUserServiceTest.class
   │        │        └─ global
   │        │           └─ initData
   │        │              └─ BaseInitDataTest.class
   │        └─ generated_tests
   ├─ settings.gradle
   └─ src
      ├─ main
      │  ├─ generated
      │  ├─ java
      │  │  └─ com
      │  │     └─ ll
      │  │        └─ coffeeBean
      │  │           ├─ domain
      │  │           │  ├─ coffeeBean
      │  │           │  │  ├─ controller
      │  │           │  │  │  └─ CoffeeBeanController.java
      │  │           │  │  ├─ dto
      │  │           │  │  │  ├─ CoffeeBeanRequestDTO.java
      │  │           │  │  │  └─ CoffeeBeanResponseDTO.java
      │  │           │  │  ├─ entity
      │  │           │  │  │  └─ CoffeeBean.java
      │  │           │  │  ├─ repository
      │  │           │  │  │  └─ CoffeeBeanRepository.java
      │  │           │  │  └─ service
      │  │           │  │     └─ CoffeeBeanService.java
      │  │           │  ├─ order
      │  │           │  │  ├─ config
      │  │           │  │  │  └─ scheduler
      │  │           │  │  │     ├─ OrderScheduler.java
      │  │           │  │  │     └─ PastOrderScheduler.java
      │  │           │  │  ├─ controller
      │  │           │  │  │  └─ OrderController.java
      │  │           │  │  ├─ dto
      │  │           │  │  │  ├─ BeanNameQuantityDTO.java
      │  │           │  │  │  ├─ GetResDetailOrderDto.java
      │  │           │  │  │  ├─ GetResMenuOrderDto.java
      │  │           │  │  │  ├─ GetResPastOrderDto.java
      │  │           │  │  │  ├─ PostDetailOrderDto.java
      │  │           │  │  │  ├─ PostOrderRequestDto.java
      │  │           │  │  │  ├─ PostOrderResponseDto.java
      │  │           │  │  │  └─ PutMenuOrderRqDTO.java
      │  │           │  │  ├─ entity
      │  │           │  │  │  ├─ DetailOrder.java
      │  │           │  │  │  ├─ MenuOrder.java
      │  │           │  │  │  └─ PastOrder.java
      │  │           │  │  ├─ enums
      │  │           │  │  │  └─ OrderStatus.java
      │  │           │  │  ├─ repository
      │  │           │  │  │  ├─ DetailOrderRepository.java
      │  │           │  │  │  ├─ OrderRepository.java
      │  │           │  │  │  └─ PastOrderRepository.java
      │  │           │  │  └─ service
      │  │           │  │     ├─ DetailOrderService.java
      │  │           │  │     ├─ OrderService.java
      │  │           │  │     └─ PastOrderService.java
      │  │           │  └─ siteUser
      │  │           │     ├─ controller
      │  │           │     │  └─ SiteUserController.java
      │  │           │     ├─ dto
      │  │           │     │  └─ CustomerDto.java
      │  │           │     ├─ entity
      │  │           │     │  └─ SiteUser.java
      │  │           │     ├─ repository
      │  │           │     │  └─ SiteUserRepository.java
      │  │           │     └─ service
      │  │           │        ├─ SiteUserService.java
      │  │           │        └─ UserService.java
      │  │           ├─ global
      │  │           │  ├─ appConfig
      │  │           │  │  ├─ AppConfig.java
      │  │           │  │  └─ CorsConfig.java
      │  │           │  ├─ aspect
      │  │           │  │  └─ ResponseAspect.java
      │  │           │  ├─ exceptions
      │  │           │  │  └─ ServiceException.java
      │  │           │  ├─ globalExceptionHandler
      │  │           │  │  └─ GlobalExceptionHandler.java
      │  │           │  ├─ initData
      │  │           │  │  └─ BaseInitData.java
      │  │           │  ├─ jpa
      │  │           │  │  └─ entity
      │  │           │  │     ├─ BaseEntity.java
      │  │           │  │     └─ BaseTime.java
      │  │           │  └─ rsData
      │  │           │     └─ RsData.java
      │  │           ├─ standard
      │  │           │  ├─ base
      │  │           │  │  └─ Empty.java
      │  │           │  ├─ PageDto
      │  │           │  │  └─ PageDto.java
      │  │           │  └─ util
      │  │           │     └─ Ut.java
      │  │           └─ WebApplication.java
      │  └─ resources
      │     ├─ application-dev.yml
      │     ├─ application-test.yml
      │     ├─ application.yml
      │     └─ logback.xml
      └─ test
         └─ java
            └─ com
               └─ ll
                  └─ coffeeBean
                     ├─ coffeeBean
                     │  └─ controller
                     │     └─ CoffeeBeanControllerTest.java
                     ├─ domain
                     │  ├─ order
                     │  │  ├─ controller
                     │  │  │  └─ OrderControllerTest.java
                     │  │  └─ service
                     │  │     └─ OrderServiceTest.java
                     │  └─ siteUser
                     │     ├─ controller
                     │     │  └─ SiteUserControllerTest.java
                     │     └─ service
                     │        └─ SiteUserServiceTest.java
                     └─ global
                        └─ initData
                           └─ BaseInitDataTest.java

```