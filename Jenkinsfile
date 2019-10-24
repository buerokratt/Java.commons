pipeline {
    agent any
    tools {
        maven 'Default'
        jdk 'Default'
    }
    stages {
        stage('Build & deploy') {
            steps {
                withCredentials([
                        usernamePassword(
                                credentialsId: 'riigiportaal-nexus',
                                usernameVariable: 'USERNAME',
                                passwordVariable: 'PASSWORD')]) {
                    withMaven() {
                        sh 'mvn clean -U deploy'
                    }
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
