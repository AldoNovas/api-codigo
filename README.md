1.- Se crea una KEY para subir los archivos a travez de SSH al repositorio de GITHUB
2.- Se configura KEY en GITHUB
3.- Realizamos el push de nuestro repositorio local al de GITHUB

"name: Java CI/CD with Maven" DEFINIMOS EL NOMBRE DE CI
---------------
"on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]"  EN ESTA PARTE ESPECIFICAMOS LOS EVENTOS CON LOS CUALES SE VA A DISPARAR NUESTRO PROCESO
--------------------
"jobs:
  build:
    runs-on: ubuntu-latest" DEFINIMOS UNA IMAGEN BASE UBUNTO  CON SU VERSION LATEST
-----------------------
"steps:
      - name: Checkout code
        uses: actions/checkout@v2" CLONAMOS EL REPOSITORIO
-----------------
"- name: Set up JDK 11
        uses: actions/setup-java@v2
        with:
          java-version: '11'
          distribution: 'adopt'" DEFINIMOS LA VERSION DEL JAVA QUE VAMOS A UTILIZAR PARA COMPILAR LA APP
---------------------
" - name: Build with Maven
        run: mvn clean install" COMPILAMOS LA APP
-----------------------
"- name: Persist target directory
        if: always()
        uses: actions/upload-artifact@v2
        with:
          name: build-artifact
          path: target/"                   EN ESTA PARTE DEFINIMOS EL DIRECTORIO TARGET/ COMO UN ARTEFACTO PERSISTENTE
--------------------
"test:
    runs-on: ubuntu-latest
    needs: build"               DEFINIMOS EL STAGE TEST QUE SE EJECUTARA EN UNA BASE UBUNTU
------------------
"steps:
      - name: Checkout code
        uses: actions/checkout@v2" NUEVAMENTE CLONAMOS EL REPOSITORIO
----------------
"  - name: Set up JDK 11
        uses: actions/setup-java@v2
        with:
          java-version: '11'
          distribution: 'adopt'"  DEFINIMOS LA VERSION JAVA
---------------------
"- name: Test with Maven
        run: |
          chmod -R 700 *
          java -version
          pwd
          echo $JAVA_HOME
          mvn -N io.takari:maven:wrapper
          ./mvnw verify"                      EN ESTE PASO PRIMERO CAMBIAMOS EL PERMISO DE EJECUCION DEL SCRIPT ./MVNW Y LO EJECUTAMOS
---------------------------
"store:
    runs-on: ubuntu-latest
    needs: test
    if: github.event_name == 'push'"  DEFINIMOS EL STORAGE DE TEST SUEMORE Y CUANDO EXISTA UN PUSH
-----------------
"steps:
      - name: Checkout code
        uses: actions/checkout@v2" CLONAMOS NUEVAMENTE EL CODIGO
---------------------
"- name: Get target directory
        uses: actions/download-artifact@v2
        with:
          name: build-artifact
          path: target/"              DESCARGAMOS EL BUILD EN DIRECTORIO TARGET
---------------------
"- name: Store JAR artifact in GitHub
        uses: actions/upload-artifact@v3
        with:
          name: unit-vs-int-0.0.1-SNAPSHOT.jar
          path: target/unit-vs-int-0.0.1-SNAPSHOT.jar" GUARDAMOS NUESTRO .JAR COMO UN ARTEFACTO
---------------
"deploy:
    runs-on: ubuntu-latest
    needs: store
    if: github.event_name == 'push'" DEFINIMOS EL STAGE DEPLOY
------------
"steps:
      - name: Deploy to Apache Maven
        run:
            pwd 
            ls -l 
            cd target 
            mvn deploy:deploy-file \
            -Durl=https://maven.pkg.github.com/AldoNovas/api-codigo \
            -Dfile=unit-vs-int-0.0.1-SNAPSHOT.jar \
            -DgroupId=danvega.dev \
            -DartifactId=unit-vs-int \
            -Dversion=0.0.1-SNAPSHOT \
            -Dpackaging=jar \
            -DrepositoryId=github \
            -DrepositoryLayout=default \
            -DuniqueVersion=false \
            -DgeneratePom=true \
            -DupdateReleaseInfo=true
        env:
          TOKEN_API_GH: ${{ secrets.TOKEN_API_GH }}"         REALIZAMOS EL DESPLIEGUE AL REPOSITORIO MAVEN
-------------------
" - name: Deploy JAR to Apache Tomcat
        run: echo "Se deployó el JAR en producción"" SIMULAMOS EL DESPLIEGUE DEL .JAR A UN AMBIENTE PROD

-----------------------------------P R E G U N T A S-----------------------------
¿Consideras útil agrega la acción de caché al workflow? Si consideras que sí, agrégala.
CONSIDERO QUE SI YA QUE ES UNA HERRAMIENTA QUE NOS PERMITE LA REDUCCION EN EL CONSUMO DE RECURSOS.

Hasta ahora solo se ha construido y publicado el código en Github Packages y aún no se ha definido la 
plataforma sobre la que se ejecutará el servicio. ¿Es posible desplegar automáticamente el artefacto 
guardado en Packages con Github Actions? Si consideras que sí, incluye el despliegue automático (no 
necesariamente debe realizar un despliegue real a una plataforma, puedes simularlo imprimiendo en consola 
el mensaje “Despliegue en curso” pues aún no se conoce en dónde se ejecutará
" - name: Deploy JAR to Apache Tomcat
   run: echo "Se deployó el JAR en producción"

El equipo de Arquitectura define que el API se desplegará en una plataforma basada en contenedores, pero 
aún están evaluando cuál será esta plataforma, se encuentran revisando Kubernetes, ECS y App Runner. 
Quieren conocer la opinión del equipo de DevOps sobre que modificaciones se tendrían que hacer al pipeline
y a la estructura del proyecto (archivos y carpetas en el repositorio). ¿Qué modificaciones consideras que se 
tendrían que realizar a los workflows para trabajar con imágenes de contenedores, siguiendo las mejores 
prácticas que conozcas
1. SE TENDRIA QUE REALIZAR EL CD
2. PRIMERO SE TENDRIA QUE COPIAR EL .JAR A UN DIRECTORIO RECURRENTE
3. TENDRIAMOS QUE DESCARGAR EL DOCKERFILE  DEL ECR
4. MODIFICAMOS EL DOCKERFILE AGREGANGO NUESTRO .JAR EN EL DIRECTORIO DE INTERPRETACION
5. AJECUTAMOS UN BUILD AL DOCKER FILE DOCKER BUILD -T NOMBRE_DE_LA_IMAGEN:TAG .
6. SUBIMOS LA IMAGEN CON PUSH A ECR
7. DEFINIMOS UNA TASK DEFINITION CON LA IMAGEN QUE ACABAMOS DE SUBIR
8. ACTUALIZAMOS LAS TASK DEFINITION 

El equipo de Arquitectura define que se utilizará ECS con Fargate y el equipo de AWS comparte el siguiente 
repositorio con plantillas de Cloudformation de ejemplo. ¿Qué pasos y/o herramientas utilizarías para 
entender las plantillas de Cloudformation y evaluar que ajustes se tendrían que realizar a la etapa de 
despliegue? (La pregunta está orientada a entender cómo abordarías el problema, no es necesario 
implementar una etapa en tú repositorio de la práctica).
NO TENGO NINGUNA EXPERIENCIA CON EL SERVICIO DE CLOUDFORMATION, PERO SE QUE SE OCUPA PARA PODER DESARROLLAR LA ARQUITECTURA DE LA INFRESTRUCTURA DE NUESTRO PROYECYO, EL CUAL NOS PUEDO CREAR LOS INSTANCIAS EC2, FARGATE, ECR, ECS.

