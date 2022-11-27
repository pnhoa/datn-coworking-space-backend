pipeline{
    agent none

    stages{
        stage("Git Stage"){
            steps{
                echo '============== Git Stage =============='
                echo 'git Version:'
                sh 'git version'
                sh 'git branch'
            }
        }

        stage("Build") {
            steps{
                echo '============== Build Stage "=============='
                echo 'Java Version:'
                sh 'java -version'
                echo 'Maven Version:'
                sh 'mvn --version'
            }
        }

        stage("Docker") {
            steps{ 
                echo '============== Docker Stage "=============='
                echo 'Docker -version'
                sh 'docker version'
            }
        }

        stage("Deploy") {
            steps{ 
                echo '============== Deploy Stage "=============='
                echo 'Docker -version'
                sh 'docker version'
            }
        }

        stage("Clean") {
            steps{ 
                echo '============== Clean Stage "=============='
                echo 'Remove source code'
                sh 'rm -rf'
            }
        }
    }
}