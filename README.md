# PropertyConfiguration

Creates and deploys an AWS Lambda function, implemented in Java.

## Prerequisites

* Created: An AWS account, and an S3 bucket for storing temporary deployment artifacts (referred to as $CF_BUCKET below)
* Installed: AWS CLI, Maven, SAM CLI
* Configured: AWS credentials in your terminal

## Usage

To build:

```
$ mvn package
```

To deploy:

```
cd scripts
./aws_deploy.sh
```

To Run Integration Tests:
Must set the following environment variables.

    AWS_ACCESS_KEY=
    AWS_SECRET_ACCESS_KEY=
    AWS_BUCKET_NAME=

