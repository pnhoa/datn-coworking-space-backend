pipeline{
    agent any

    environment {
        // Docker Registry
        REGISTRY_URL = '056666214492.dkr.ecr.ap-southeast-1.amazonaws.com'
        REGISTRY_REPOSITORY= 'coworking-space-backend'             
    }

    stages{
        stage("Build") {
            steps{
                echo 'Java Version:'
                sh 'java -version'
                echo 'Maven Version:'
                sh 'mvn --version'
                sh 'mvn -Dmaven.test.skip clean install spring-boot:repackage'
            }
        }

        stage("Docker") {
            steps{ 
                echo 'Docker -version'
                sh 'docker version'
                docker.withRegistry("https://${REGISTRY_URL}", REGISTRY_CREDENTIAL) {
                    def image = docker.build("${REGISTRY_URL}/${REGISTRY_REPOSITORY}:latest",".")
                    image.push()
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