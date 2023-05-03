# helloworldapi

## in order to setup it on local following steps needs to be done
- Get mysql installed on your local system, else you can deploy a MYSQL server on cloud and create app database
- there after configure the following properties in below files  
\
Filename: \
helloworldapi/src/main/resources/application.properties for dev environment \
helloworldapi/src/main/resources/application-docker.properties for docker environment \

- properties which needs to be changed is shown below \
  spring.datasource.url=jdbc:mysql://<ip>:<port>/app \
  spring.datasource.username=\<user> \
  spring.datasource.password=\<password>

- now either run using IDE or build and run the created docker continer directly on your local docker environment \
Note: to build the dokcer image \ 
just write "docker build -t helloworldapi ." \
and to execute it write and hit "docker run -p 8080:8080 helloworldapi" 

- moreover,if you have your own jenkins environment then you can create a CI/CD pipeline to deploy the application on GCP or any kubernetes environment whenever you commmit a new code. 

## in order to setup it on jenkins
create a freestyle project and configure it as below
<img width="1439" alt="Screenshot 2023-05-03 at 4 40 17 PM" src="https://user-images.githubusercontent.com/39569070/236027821-e1750b20-5520-4f7b-80a2-ef7a6f7fe55b.png">

now setup the source control as per your choice here i have used github repo so my configurations are as per below
<img width="1400" alt="Screenshot 2023-05-03 at 4 43 09 PM" src="https://user-images.githubusercontent.com/39569070/236028778-a011bc49-0a76-4737-94cf-5eb79d0efad3.png">

<img width="1398" alt="Screenshot 2023-05-03 at 4 44 08 PM" src="https://user-images.githubusercontent.com/39569070/236029189-ab31cf12-a8dd-43d2-8869-b544fa134cb4.png">

setup Build Triggers as per your choice here I am using polling mechanism which will check every minute for new update on your repository
<img width="1428" alt="Screenshot 2023-05-03 at 4 44 30 PM" src="https://user-images.githubusercontent.com/39569070/236029360-f4e0fda2-61a9-480f-9bb3-1e97dea20034.png">

now Add Build step as below, here we are going to use maven to build our package
<img width="1070" alt="Screenshot 2023-05-03 at 4 45 34 PM" src="https://user-images.githubusercontent.com/39569070/236029840-68f2cb48-b1e1-41a3-afe1-e3cdbdba5b2e.png">

setup Docker build and push repositoyr as below
<img width="1394" alt="Screenshot 2023-05-03 at 4 46 30 PM" src="https://user-images.githubusercontent.com/39569070/236030172-d136f011-8a93-4fd5-8aeb-49f177c3d16e.png">

provide your credentials and save it. 

## now we will create another pipeline which will help us to setup our credentials and of GCP and Deployment steps

configure it as shown below

first we will setup a build trigger which would be done buy our main builing peipline which we configured earlier
<img width="1436" alt="Screenshot 2023-05-03 at 4 50 08 PM" src="https://user-images.githubusercontent.com/39569070/236031192-987f08d6-1061-493e-a34d-513be7120e2c.png">

now write pipeline script as below,
<pre><code>
pipeline {
  agent any
  stages {
    stage('setup cred') {
      steps {
        withCredentials([file(credentialsId: 'gcloud-creds', variable: 'GCLOUD_CREDS')]) {
          sh '''
            gcloud version
            gcloud auth activate-service-account --key-file="$GCLOUD_CREDS"
            gcloud container clusters get-credentials autopilot-cluster-1  --region=us-central1
          '''
        //   sh "chmod +x -R ${env.WORKSPACE}"
        //   sh 'helm install helloworldapichart ./helloworldapichart/'
        }
      }
    }
    stage('deploy'){
        steps{
            git branch: "main", url: "https://github.com/rsushmanth/helloworldapi.git"
            // sh 'helm install helloworldapichart ./helloworldapichart/'
            sh 'kubectl apply -f ./kube-helloworld-deployment.YAML'
        }
    }
  }
}
</code>
</pre>

## Testing the API

- there is a one endpoint, which will accept put and get http requests. 
  inorder to add a new user or to update the date of birth just send a put request on \<ip>:\<port>/hello/\<username>
  with having a following JSON as body
  <code>
  {
    "dateOfBirth":"1999-05-24"
  }
  </code>
  
    <img width="886" alt="Screenshot 2023-05-03 at 5 33 35 PM" src="https://user-images.githubusercontent.com/39569070/236043185-21cfa5e6-74f0-492f-a709-690fa28f368c.png">
    
- sending a get request on same endpoint, will greet a user with saying how many days are left till users birthday, if it is today then it will wish him/her.
    <img width="846" alt="Screenshot 2023-05-03 at 5 36 58 PM" src="https://user-images.githubusercontent.com/39569070/236043866-2f4ff102-ee6f-4401-8de2-1be788ffc2ac.png">
