terraform {
  // aws 라이브러리 불러옴
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

# AWS 설정 시작
provider "aws" {
  region = var.region
}
# AWS 설정 끝

# S3 설정 시작
# bucket_1 : 공개 버킷
resource "aws_s3_bucket" "bucket_1" {
  bucket = "${var.prefix}-bucket-${var.nickname}-1"

  tags = {
    Name = "${var.prefix}-bucket-${var.nickname}-1"
  }
}

data "aws_iam_policy_document" "bucket_1_policy_1_statement" {
  statement {
    sid    = "PublicReadGetObject"
    effect = "Allow"

    principals {
      type        = "AWS"
      identifiers = ["*"]
    }

    actions   = ["s3:GetObject"]
    resources = ["${aws_s3_bucket.bucket_1.arn}/*"]
  }
}

resource "aws_s3_bucket_policy" "bucket_1_policy_1" {
  bucket = aws_s3_bucket.bucket_1.id

  policy = data.aws_iam_policy_document.bucket_1_policy_1_statement.json

  depends_on = [aws_s3_bucket_public_access_block.bucket_1_public_access_block_1]
}

resource "aws_s3_bucket_public_access_block" "bucket_1_public_access_block_1" {
  bucket = aws_s3_bucket.bucket_1.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

# bucket_2 : 비공개 버킷
resource "aws_s3_bucket" "bucket_2" {
  bucket = "${var.prefix}-bucket-${var.nickname}-2"

  tags = {
    Name = "${var.prefix}-bucket-${var.nickname}-2"
  }
}

resource "aws_s3_object" "object_1" {
  bucket       = aws_s3_bucket.bucket_1.id
  key          = "/index.html"
  content      = "Hello"  # 직접 문자열 사용
  content_type = "text/html"
  etag       = md5("Hello")
  depends_on = [aws_s3_bucket.bucket_2]
}

resource "aws_s3_object" "object_2" {
  bucket       = aws_s3_bucket.bucket_2.id
  key          = "/index.html"
  content      = "Hello"  # 직접 문자열 사용
  content_type = "text/html"
  etag       = md5("Hello")
  depends_on = [aws_s3_bucket.bucket_2]
}
# S3 설정 끝