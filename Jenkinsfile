pipeline {
    agent any
    tools {
        maven 'default'
        jdk 'default'
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn -Dmaven.test.failure.ignore=false install'
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