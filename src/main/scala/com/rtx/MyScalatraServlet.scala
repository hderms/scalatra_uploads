package com.rtx

import org.scalatra._
import org.scalatra.servlet.{FileUploadSupport, MultipartConfig, SizeConstraintExceededException}
import xml.Node
import awscala._, s3._
import scala.io.Source



class MyScalatraServlet extends UploadsStack with FileUploadSupport {


  implicit val s3 = S3()
  configureMultipartHandling(MultipartConfig(maxFileSize = Some(3*1024*1024)))

  get("/") {
      <form action={url("/upload")} method="post" enctype="multipart/form-data">
      <p>File to upload: <input type="file" name="file" /></p>
      <p><input type="submit" value="Upload" /></p>
      </form>
  }
  post("/upload") {
    val bucket: Bucket = s3.createBucket("unique-name-xxx")
    println(fileParams.get("file"))
    fileParams.get("file") match {

      case Some(file) =>
        val tempFile: java.io.File = java.io.File.createTempFile("foo", "bar")
        val fos: java.io.FileOutputStream = new java.io.FileOutputStream(tempFile);
        fos.write(file.get());
        fos.close();
        bucket.put("sample.txt", tempFile)
          <html>
          <p>
          uploaded
          </p>
          </html>
      case None =>
        println("heh")
        <html>
          <p>
          Hey! You forgot to select a file.
          </p>
          </html>
    }

  }

}
