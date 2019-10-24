pipeline {
    tools {
        maven 'Default'
        jdk 'Default'
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn clean -U deploy'
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
