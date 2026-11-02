# QR Code Generator

A Spring Boot application that generates QR codes and stores them in AWS S3. This project demonstrates the integration of Google's ZXing library for QR code generation and AWS S3 for storage.


## How to Use

### Prerequisites

- Java 21 JDK
- Maven
- Docker
- AWS Account with S3 access
- An IAM user with an access key and permission to read and write in your S3 bucket

### Environment Variables

Create a `.env` file in the project root with the following variables:

```env
AWS_ACCESS_KEY_ID=your_access_key
AWS_SECRET_ACCESS_KEY=your_secret_key
AWS_REGION=your_region
AWS_BUCKET_NAME=your_bucket_name
```

> **Warning:** Never commit your `.env` file. Make sure it is listed in your `.gitignore`, otherwise your AWS credentials can be exposed.

### Running the Application

#### Local Development

1. Create the `.env` file as described above
2. Load the variables into your terminal session:

   ```bash
   export $(grep -v '^#' .env | xargs)
   ```

3. Build the project:

   ```bash
   mvn clean package
   ```

4. Run the application:

   ```bash
   mvn spring-boot:run
   ```

#### Docker Deployment

1. Build the Docker image:

   ```bash
   docker build -t qrcode-generator:1.0 .
   ```

   Remember to replace the version and image name if you want.

2. Run the container:

   ```bash
   docker run --env-file .env -p 8080:8080 qrcode-generator:1.0
   ```

   Remember to replace `.env` with the path to the environment file that you created.

### AWS S3 Configuration

1. Create an S3 bucket in your AWS account (**S3 > Create bucket**)
2. Create an IAM user (**IAM > Users > Create user**) with the `AmazonS3FullAccess` policy, or a policy restricted to your bucket, and generate an access key for it
3. Update `AWS_BUCKET_NAME` and `AWS_REGION` in your `.env` file. The region must be the same one where the bucket was created
4. Make the generated files readable through the returned URL. In **Permissions > Block public access**, disable "Block all public access", then add this policy in **Permissions > Bucket policy** (replace `your_bucket_name`):

   ```json
   {
     "Version": "2012-10-17",
     "Statement": [
       {
         "Sid": "PublicRead",
         "Effect": "Allow",
         "Principal": "*",
         "Action": "s3:GetObject",
         "Resource": "arn:aws:s3:::your_bucket_name/*"
       }
     ]
   }
   ```

## API Endpoints

### POST /qrcode

Generate a QR code from the provided text and store it in AWS S3. The QR code will be generated as a PNG image with dimensions of 200x200 pixels.

**Parameters**

| Name | Required | Type | Description |
| ---- | -------- | ---- | ----------- |
| text | required | string | The text content to be encoded in the QR code. This can be any string value that you want to convert into a QR code. |

**Response**

```json
{
    "url": "https://your-bucket.s3.your-region.amazonaws.com/random-uuid"
}
```

