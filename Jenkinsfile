pipeline {
    agent any
    tools {
        maven 'default'
        jdk 'default'
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn clean -U install'
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