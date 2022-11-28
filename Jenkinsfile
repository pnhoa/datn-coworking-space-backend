pipeline {
    agent any

    environment {
        REGISTRY_HOST = '056666214492.dkr.ecr.ap-southeast-1.amazonaws.com'
        REGISTRY_CREDENTIAL = 'ecr:ap-southeast-1:coworking-space-backend'             
        REGISTRY_REPOSITORY = 'coworking-space-backend'             
    }

    stages {
        stage("Build") {
            steps{
                sh 'java -version'
                sh 'mvn --version'
                sh 'mvn -Dmaven.test.skip clean install spring-boot:repackage'
            }
        }

        stage("Docker") {
            steps{ 
                sh 'docker version'  
                script { 
                    docker.withRegistry("https://${REGISTRY_HOST}", REGISTRY_CREDENTIAL) {
                        def image = docker.build("${REGISTRY_HOST}/${REGISTRY_REPOSITORY}:latest",".")
                        image.push()
                    }   
                }
            }
        }

        stage("Deploy") {
            steps{ 
                echo 'ssh to deploy server'
            }
        }
    }

    post { 
        always {
            deleteDir()
        }
    }
}