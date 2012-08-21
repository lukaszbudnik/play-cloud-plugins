package plugins.cloudimage

import org.specs2.mutable._
import play.api.Play.current
import play.api.test.Helpers.inMemoryDatabase
import play.api.test.Helpers.running
import play.api.test.FakeApplication
import plugins.use
import java.io.BufferedInputStream
import java.io.FileInputStream

class CloudinaryCloudImageSpec extends Specification {

  private val fileName = "logo.png"

  private val transformationProperties = Map[TransformationProperty.Value, String](
    TransformationProperty.WIDTH -> "125",
    TransformationProperty.HEIGHT -> "125",
    TransformationProperty.CROP_MODE -> "c_fit");

  "CloudinaryCloudImagePlugin" should {
    "upload and destroy an image" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val cloudImageService = use[CloudImagePlugin].cloudImageService

        val bis = new BufferedInputStream(new FileInputStream("public/" + fileName))
        val contents = Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray

        val uploadResponse = cloudImageService.upload(fileName, contents)
        uploadResponse must haveClass[CloudImageSuccessResponse]

        val publicId = uploadResponse.asInstanceOf[CloudImageSuccessResponse].publicId

        val destroyResponse = cloudImageService.destroy(publicId)
        destroyResponse must haveClass[CloudImageDestroySuccessResponse]
      }
    }
    "get transformation URL" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val cloudImageService = use[CloudImagePlugin].cloudImageService

        val transformationUrl = cloudImageService.getTransformationUrl("url", transformationProperties)

        transformationUrl must contain("w_125,h_125,c_fit")
      }
    }
  }

}