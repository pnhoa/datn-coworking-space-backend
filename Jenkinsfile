pipeline{
    agent any

    stages{
        stage("Build") {
            steps{
                echo 'Java Version:'
                sh 'java -version'
                echo 'Maven Version:'
                sh 'mvn --version'
            }
        }

        stage("Docker") {
            steps{ 
                echo 'Docker -version'
                sh 'docker version'
            }
        }

        stage("Deploy") {
            steps{ 
                echo 'ssh to deploy server'
            }
        }

        stage("Clean Up") {
            steps{ 
                deleteDir()
            }
        }
    }
}