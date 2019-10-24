pipeline {
    agent any
    tools {
        maven 'Default'
        jdk 'Default'
    }
    stages {
        stage('Build & deploy') {
            steps {
                cleanWs()
                withMaven() {
                    sh 'mvn dependency:list-repositories'
                    sh 'mvn clean -U deploy'
                }
            }
            post {
                success {
                    sh 'echo "success"'
                }
                failure {
                    sh 'echo "fail"'
                }
            }
        }
    }
}
