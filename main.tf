locals {
  app_name  = "springboot-reference"
}

terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
#
#  backend "s3" {
#    bucket = "terraform-state-bucket"
#    key    = "state/terraform_state.tfstate"
#    region = "ap-southeast-2"
#  }
}

provider "aws" {
  region     = "ap-southeast-2"
}

# ecr.tf | Elastic Container Repository

resource "aws_ecr_repository" "aws-ecr" {
  name = "${local.app_name}-ecr"
}