# Open-source, Serverless web-mapping on AWS

## Purpose
This repository holds all the code, instructions, and documentation related to my Penn State MGIS capstone project.

The purpose of this project is to create a geospatial data management and visualization tool to make sense of all the data inundating the agriculture industry


[![Build Status](https://travis-ci.com/atxFarley/aws_serverless.svg?branch=master)](https://travis-ci.com/atxFarley/aws_serverless) [![GitHub license](https://img.shields.io/github/license/Naereen/StrapDown.js.svg)](https://github.com/atxFarley/aws_serverless/blob/master/LICENSE.txt) [![Open Source? Yes!](https://badgen.net/badge/Open%20Source%20%3F/Yes%21/blue?icon=github)](https://github.com/Naereen/badges/)

<img src="https://img.shields.io/badge/AWS%20-%23FF9900.svg?&style=for-the-badge&logo=amazon-aws&logoColor=white"/> <img src ="https://img.shields.io/badge/postgres-%23316192.svg?&style=for-the-badge&logo=postgresql&logoColor=white"/> <img src="https://img.shields.io/badge/angular%20-%23DD0031.svg?&style=for-the-badge&logo=angular&logoColor=white"/> <img src="https://img.shields.io/badge/bootstrap%20-%23563D7C.svg?&style=for-the-badge&logo=bootstrap&logoColor=white"/> <img src="https://img.shields.io/badge/java-%23ED8B00.svg?&style=for-the-badge&logo=java&logoColor=white"/> <img src="https://img.shields.io/badge/typescript%20-%23007ACC.svg?&style=for-the-badge&logo=typescript&logoColor=white"/> <img src="https://img.shields.io/badge/javascript%20-%23323330.svg?&style=for-the-badge&logo=javascript&logoColor=%23F7DF1E"/>

 

## Motivation
The project was inspired by Penn State coursework, my professional experience as a software developer, and personal relationships with rural agriculture professionals.  

The data in the ag industry is coming from a variety of sources including satellite imagery, drone imagery, GPS sensors on farm equipment, field sensors, weather sensors, soil samples, as well as the old-fashioned visual observations.  The data is both vector and raster data formats and has an important temporal component.  

This is an intimidating amount of data for any individual grower to negotiate. 

Multiply that data for an agriculture consultant, growers’ association, or agriculture retailer servicing multiple growers, this amount increases exponentially and the management/analysis of it all requires a robust, scalable solution.  

The focus for this project is specifically on crop growers and organizations that access and/or manage field data for growers such as farm managers, agriculture retailers, grower associations, crop consultants, and agronomists.

## Project Goals
The application should be easily configured and deployed to cloud-computing platform with ZERO infrastructure management. 

* Web-mapping application complete with data storage.
* Use only readily-available cloud services (this project is specific to AWS)
* Use only open-source frameworks
* No VMs!!

## Cloud Computing Platform
This project was written specifically for deployment to AWS.  While the database and UI pieces are platform-agnostic, the "middle-layer" of the application is a collection of AWS Lambda functions.
A brief list of the main AWS services required for this application: 

* Amazon Relational Database Service (Amazon RDS) - PostgreSQL database engine with PostGIS extensions
* Amazon S3
* AWS Lambda
* AWS Amplify Console

This is not a complete list.  Further AWS toolkits and services are documented in the configuration instructions. 

## Tech/Frameworks
Open-source was a key goal of this project.  This project should be accessible and free to use, modify, distribute as needs evolve.  
Costs of running this application should be limited to AWS service usage only.  
#### Database
* PostgreSQL with PostGIS extensions

#### Lambda - serves as the layer between database and UI (and the UI and S3 bucket)
* Written in Java

#### UI
* Angular 10
* Bootstrap
* Leaflet
    * There is one potential additional expense within the web-mapping interface depending on basemap services used. 
    * For this project, a personal mapbox service is used for satellite imagery basemap option

## Key Features
* Open-source!
* Serverless - absolutely no EC2 instances.  

    As a software developer, maintaining infrastructure (configuring virtual machines, allocating appropriate sizes) is soul-sucking.  
    I wanted to create a solution that required absolutely no VMs.  

    With cloud-computing platforms expanding services that manage underlying infrastructure for you, this was an opportunity to build out an entire application using ONLY readily-available services. 

* Complete solution that can be forked and deployed to subsequent AWS accounts with minimal configuration.

## Project Usage Prerequisites
1. AWS Account
2. Familiarity with cloud-computing, web application architecture/frameworks, and serverless/Function-as-a-Service (FaaS) computing model

## Get Started Using this Project
Instructions for project fall into 5 larger sequential steps: 
1. _AWS Account Setup_
2. _Development Environment Configuration_
3. _Amazon RDS Service Creation/Configuration_
4. _AWS Lambda Setup_
5. _UI Configuration/Deployment_

For anyone new to serverless, The AWS Serverless Web Applications tutorial is a good staring point. 
[AWS Serverless Web Applications](https://aws.amazon.com/lambda/web-apps/)

Throughout this documentation, references to AWS documentation will be provided for more in-depth explanation and instruction.

### AWS Account Setup
#### AWS Identity and Access Management (IAM) 
1. AWS recommends creating an IAM User with administrator permissions as a best practice to protect root user credentials. 
    1. Follow instructions [here](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_SettingUp.html#CHAP_SettingUp.IAM) to create an Administrator user and add to an administrators group. 
2. Create another IAM User with Programmatic access. This is the user that will be used by AWS CLI.  Read more about creating IAM users [here](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html).
    1. Attach the AdministratorAccess policy to this user as well.  
    2. You will need to create access keys for this IAM user by following [these instructions](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-quickstart.html)
    3. Download and store your key file for this user in a secure location.  You will use this later.  
3.  Execution roles are required for the AWS Lambda services
    1. From the IAM Dashboard, Create role: 
        * __Trusted entity:__ Lambda
        * __Permission Policy:__ AWSLambdaBasicExecutionRole
        * __Role Name:__ lambda-basic-role
    2. Create a 2nd role for Lambda functions accessing S3 Bucket:
        * __Trusted entity:__ Lambda
        * __Permission Policy:__ AWSLambdaExecute, AmazonS3FullAccess
        * __Role Name:__ lambda-s3-role
#### Amazon S3
This project requires a bucket that is publicly accessible.  It stores files associated with database records that must be viewable to users of the web applicaiton. 
1. Create Bucket: 
    * __Bucket name:__ieldactivityfiles
    * __Permissions --> Access Control List:__Allow Everyone List Objects
    * __Permissions --> Cross-origin resource sharing (CORS):__
    ````
    <CORSConfiguration>
    <CORSRule>
    <AllowedOrigin>*</AllowedOrigin>
    <AllowedMethod>HEAD</AllowedMethod>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedMethod>POST</AllowedMethod>
    <AllowedMethod>DELETE</AllowedMethod>
    <ExposeHeader>ETag</ExposeHeader>
    <AllowedHeader>*</AllowedHeader>
    </CORSRule>
    </CORSConfiguration>
    ````
   * __Permissions --> Bucket Policy:__
   ````
   {
       "Version": "2012-10-17",
       "Statement": [
           {
               "Sid": "PublicReadGetObject",
               "Effect": "Allow",
               "Principal": "*",
               "Action": "s3:GetObject",
               "Resource": "arn:aws:s3:::fieldactivityfiles/*"
           }
       ]
   }
   ````
Further AWS Service configuration will be documented in subsequent steps.  

### Development Environment Configuration

IntelliJ IDEA users rejoice!  The [AWS Toolkit for Jetbrains](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html) makes serverless application development and deployment super easy.

Follow [these instructions](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/setup-toolkit.html) to install the Toolkit AND the necessary tools required by the Toolkit for AWS serverless/Lambda function development, testing, and deployment. 
1. [AWS Command Line Interface (AWS CLI)](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)
    * Note: You will use the key file saved earlier of the IAM Administrator user with Programmatic access when configuring the AWS CLI.
2. [Docker](https://docs.docker.com/install/)
3. [AWS Serverless Application Model Command Line Interface (AWS SAM CLI)](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html)

I did test out both  [Serverless Framework Open Source](https://www.serverless.com/open-source/) and AWS SAM CLI.  Both were easy to use, but since the AWS Toolkit uses AWS SAM CLI, I chose that route.
Deployment from IntelliJ is as simple as right-click --> Deploy from the AWS SAM template.yaml file.  

I found it worthwhile to spend some time understanding [AWS SAM](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/what-is-sam.html)
Besides its CLI, AWS SAM uses [template specification](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-specification.html) to define each function.  

This template.yaml file can be used to deploy multiple functions as one serverless application or each Lambda function can be a single application.  
*__For this application, all the functions are defined in a single template.yaml and deployed as one serverless application.__* 



### AWS Service Configuration/Code Usage 
#### Database

#### AWS Lambda

#### UI


## API Reference

## Tests

### Lambda 

### Angular UI

## Roadmap

## Maintainer
[atxFarley](https://github.com/atxFarley)

## Credits
* [Supported by the AWS Cloud Credits for Research program](https://aws.amazon.com/research-credits/)
* [Penn State World Campus Master of Geographic Information Systems](https://www.worldcampus.psu.edu/degrees-and-certificates/geographic-information-systems-gis-masters/overview)
* [MGIS Advisor Ryan Baxter](https://www.geog.psu.edu/directory/ryan-baxter)

## Contribute


## License

[MIT © 2020 Amy Farley](LICENSE.txt)




 







