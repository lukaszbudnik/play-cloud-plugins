package plugins.cloudimage
import org.apache.commons.lang3.StringUtils

abstract class CloudImageResponse

case class CloudImageErrorResponse(val message: String) extends CloudImageResponse

case class CloudImageSuccessResponse(val url: String, val secureUrl: String,
  val publicId: String, val version: String, val width: String, val height: String,
  val format: String, val resourceType: String, val signature: String) extends CloudImageResponse

case class CloudImageDestroySuccessResponse(val result: String) extends CloudImageResponse

trait CloudImageService {
  def upload(filename: String, fileInBytes: Array[Byte]): CloudImageResponse
  def destroy(publicId: String): CloudImageResponse
  def getTransformationUrl(url: String, properties: Map[TransformationProperty.Value, String]): String
}

trait CloudImagePlugin extends play.api.Plugin {
  def cloudImageService: CloudImageService
}

class MockCloudImageService extends CloudImageService {

  def upload(filename: String, fileInBytes: Array[Byte]): CloudImageResponse = {
    CloudImageSuccessResponse("url", "secureUrl", "publicId", "1", "1", "1", "png", "image", "signature")
  }

  def destroy(publicId: String): CloudImageResponse = {
    CloudImageDestroySuccessResponse("ok")
  }

  def getTransformationUrl(url: String, properties: Map[TransformationProperty.Value, String]): String = {
    val sb = new StringBuilder();
    var first = false;

    properties.get(TransformationProperty.WIDTH) match {
      case Some(x) =>
        if (!StringUtils.isEmpty(x)) {
          sb.append("w_").append(x)
          first = true;
        }
      case _ =>
    }

    properties.get(TransformationProperty.HEIGHT) match {
      case Some(x) =>
        if (!StringUtils.isEmpty(x)) {
          if (first) sb.append(",")
          else first = true
          sb.append("h_").append(x)
        }
      case _ =>
    }

    properties.get(TransformationProperty.CROP_MODE) match {
      case Some(x) =>
        if (first) sb.append(",")
        else first = true
        if (!StringUtils.isEmpty(x)) {
          sb.append(x)
        }
      case _ =>
    }
    sb.toString()
  }
}

object TransformationProperty extends Enumeration {

  val HEIGHT = Value("height")
  val WIDTH = Value("width")
  val CROP_MODE = Value("cropMode")
}

 