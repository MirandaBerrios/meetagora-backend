![](https://capstone-396701.uc.r.appspot.com//meetagora-services/gcp/images/meetagora-logo.png)

### Resumen
Meetagora-backend es una aplicación desarrollada en java 8 con spring boot 2.5.7, sus dependencias son gestionadas son maven.
Para que el software pueda funcionar son necesarios ciertos requerimientos:
- Installar jre1.8_3xx .
- Contar con almenos 500mb disponibles en el disco.
- Contar con la carpeta logs en el directorio en donde se correrá el jar.
- Habilitar el tráfico en el puerto 8080 , que es el puerto por defecto que ocupa el servicio.
- Contar con navegador compatible con las funcionalidades del software, es decir Google Chrome, Mozilla FireFox, Safari. Estos deben estar con la actualización más reciente.


# Disponibilidad
Meetagora-backend no necesita ser instalado para poder disgrutar de la aplicación, ya que está desplegado en la nube,
puedes simplemente descargar nuestra app desde el siguiente link ([https://drive.google.com/drive/folders/1PiKFIPXpObn2sUkcR6kRHIQ_s9fMHFBa?usp=drive_link](https://storage.cloud.google.com/front-apk/meetagora-app-release.apk)).


# Imagen
Otra opción , es crear la imagen de meetagora a través del docker, lo que hará que meetagora-backend implemente la imagen en un container para poder servir.
- Clona este repositorio, en la parte superior encontrarás el botón clonar, verifica que cuentes con los permisos para poder clonar el código.
- Cuando hayas clonado exitosamente el proyecto puedes crear una imagen usando el Dockerfile que ya está configurado para poder crear la imagen.

`$ docker build -t meetagora-image`

posteriormente sólo deberás correr la imagen

  `$docker run meetagora-image`
