pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9' // Make sure this matches your Jenkins Maven installation name
        jdk 'JDK-17'      // Make sure this matches your Jenkins JDK installation name
    }
    
    environment {
        // MySQL connection parameters for testing
        MYSQL_HOST = 'localhost'
        MYSQL_PORT = '3306'
        MYSQL_DATABASE = 'travel_agency_g8'
        MYSQL_CREDENTIALS = credentials('mysql-credentials') // Configure this in Jenkins
        
        // Email notification settings
        EMAIL_RECIPIENTS = 'thesecondjl@gmail.com' // Update with your email
        
        // Artifact settings
        ARTIFACT_NAME = "proyecto_g8-${env.BUILD_NUMBER}.jar"
        DEPLOY_DIR = 'C:\\Jenkins\\deployments\\proyecto_g8' // Windows path for local deployment
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '====== Checking out code from repository ======'
                checkout scm
                echo "Branch: ${env.GIT_BRANCH}"
                echo "Commit: ${env.GIT_COMMIT}"
            }
        }
        
        stage('Build') {
            steps {
                echo '====== Building project with Maven ======'
                bat 'mvn clean compile'
            }
            post {
                success {
                    echo 'Build completed successfully!'
                }
                failure {
                    echo 'Build failed!'
                }
            }
        }
        
        stage('Test') {
            steps {
                echo '====== Running unit tests ======'
                bat 'mvn test'
            }
            post {
                always {
                    // Publish test results
                    junit '**/target/surefire-reports/*.xml'
                    
                    // Publish JaCoCo code coverage report
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        exclusionPattern: '**/test/**'
                    )
                }
                success {
                    echo 'All tests passed!'
                }
                failure {
                    echo 'Some tests failed!'
                }
            }
        }
        
        stage('Code Quality Analysis') {
            steps {
                echo '====== Running code quality checks ======'
                bat 'mvn checkstyle:checkstyle'
            }
            post {
                always {
                    // Publish Checkstyle report
                    recordIssues(
                        enabledForFailure: true,
                        tool: checkStyle(pattern: '**/target/checkstyle-result.xml')
                    )
                }
            }
        }
        
        stage('Package') {
            steps {
                echo '====== Packaging application ======'
                bat 'mvn package -DskipTests'
            }
            post {
                success {
                    echo 'Application packaged successfully!'
                    archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
                }
            }
        }
        
        stage('Deploy to Test Environment') {
            steps {
                echo '====== Deploying to test environment ======'
                script {
                    // Create deployment directory if it doesn't exist
                    bat """
                        if not exist "${DEPLOY_DIR}" mkdir "${DEPLOY_DIR}"
                    """
                    
                    // Copy the JAR file to deployment directory
                    bat """
                        copy /Y target\\proyecto_g8-1.0-SNAPSHOT-jar-with-dependencies.jar "${DEPLOY_DIR}\\${ARTIFACT_NAME}"
                    """
                    
                    // Create a symlink or copy to latest version
                    bat """
                        copy /Y "${DEPLOY_DIR}\\${ARTIFACT_NAME}" "${DEPLOY_DIR}\\proyecto_g8-latest.jar"
                    """
                    
                    echo "Application deployed to: ${DEPLOY_DIR}"
                    echo "Artifact: ${ARTIFACT_NAME}"
                }
            }
            post {
                success {
                    echo 'Deployment successful!'
                }
                failure {
                    echo 'Deployment failed!'
                }
            }
        }
        
        stage('Generate Reports') {
            steps {
                echo '====== Generating Maven site reports ======'
                bat 'mvn site'
            }
            post {
                always {
                    // Publish HTML reports
                    publishHTML(target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/site',
                        reportFiles: 'index.html',
                        reportName: 'Maven Site Report'
                    ])
                }
            }
        }
    }
    
    post {
        always {
            echo '====== Pipeline execution completed ======'
            // Clean workspace after build
            cleanWs(deleteDirs: true, patterns: [[pattern: 'target/**', type: 'INCLUDE']])
        }
        
        success {
            echo '====== Pipeline succeeded! ======'
            emailext(
                subject: "✅ SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: """
                    <html>
                    <body>
                        <h2>Build Success!</h2>
                        <p><b>Job:</b> ${env.JOB_NAME}</p>
                        <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                        <p><b>Branch:</b> ${env.GIT_BRANCH}</p>
                        <p><b>Status:</b> <span style="color:green">SUCCESS</span></p>
                        <p><b>Duration:</b> ${currentBuild.durationString}</p>
                        <p><b>Deployed to:</b> ${DEPLOY_DIR}</p>
                        <p><b>Artifact:</b> ${ARTIFACT_NAME}</p>
                        <hr>
                        <p>Check console output at: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                    </body>
                    </html>
                """,
                to: "${EMAIL_RECIPIENTS}",
                mimeType: 'text/html'
            )
        }
        
        failure {
            echo '====== Pipeline failed! ======'
            emailext(
                subject: "❌ FAILURE: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: """
                    <html>
                    <body>
                        <h2>Build Failed!</h2>
                        <p><b>Job:</b> ${env.JOB_NAME}</p>
                        <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                        <p><b>Branch:</b> ${env.GIT_BRANCH}</p>
                        <p><b>Status:</b> <span style="color:red">FAILURE</span></p>
                        <p><b>Duration:</b> ${currentBuild.durationString}</p>
                        <hr>
                        <p>Check console output at: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                        <p>Please investigate and fix the issues.</p>
                    </body>
                    </html>
                """,
                to: "${EMAIL_RECIPIENTS}",
                mimeType: 'text/html'
            )
        }
        
        unstable {
            echo '====== Pipeline is unstable! ======'
            emailext(
                subject: "⚠️ UNSTABLE: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: """
                    <html>
                    <body>
                        <h2>Build Unstable!</h2>
                        <p><b>Job:</b> ${env.JOB_NAME}</p>
                        <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                        <p><b>Branch:</b> ${env.GIT_BRANCH}</p>
                        <p><b>Status:</b> <span style="color:orange">UNSTABLE</span></p>
                        <p><b>Duration:</b> ${currentBuild.durationString}</p>
                        <hr>
                        <p>Check console output at: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                        <p>Some tests may have failed or code quality issues detected.</p>
                    </body>
                    </html>
                """,
                to: "${EMAIL_RECIPIENTS}",
                mimeType: 'text/html'
            )
        }
    }
}
