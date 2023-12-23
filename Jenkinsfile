pipeline {
    agent any
    stages {
        stage('Unit & Integration Tests') {
            steps {
                script {
                    try {
                        sh 'pwd'
                        sh 'ls -la'
                        sh 'sudo ./gradlew clean test --no-daemon'
                    } finally {
                        junit '**/build/test-results/test/*.xml'
                    }
                }
            }
        }
        stage('Build project as jar') {
            steps {
                sh 'sudo ./gradlew build --no-daemon'
            }
        }
    }
}