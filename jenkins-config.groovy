/**
 * Jenkins Pipeline Configuration Script
 * 
 * This script can be used to create and configure a Jenkins pipeline job programmatically
 * using Jenkins Job DSL or as a reference for manual configuration.
 */

// Job Configuration
def jobName = 'simple_java_CRUD_S8_Pipeline'
def repoUrl = 'https://github.com/Btlguse/simple_java_CRUD_S8.git' // Update with your repo URL
def gitBranch = '*/feature/JAVIER'
def gitCredentialsId = 'github-credentials' // Configure in Jenkins

pipelineJob(jobName) {
    description('CI/CD Pipeline for Travel Agency Management System - Java CRUD Application')
    
    properties {
        // GitHub project URL
        githubProjectUrl(repoUrl)
        
        // Build discarder - keep last 10 builds
        buildDiscarder {
            strategy {
                logRotator {
                    daysToKeepStr('30')
                    numToKeepStr('10')
                    artifactDaysToKeepStr('30')
                    artifactNumToKeepStr('5')
                }
            }
        }
        
        // Disable concurrent builds
        disableConcurrentBuilds()
    }
    
    parameters {
        // Add build parameters
        stringParam('DEPLOY_ENV', 'test', 'Deployment environment (test/staging/prod)')
        booleanParam('SKIP_TESTS', false, 'Skip running tests')
        choiceParam('LOG_LEVEL', ['INFO', 'DEBUG', 'WARN', 'ERROR'], 'Logging level')
    }
    
    triggers {
        // GitHub webhook trigger
        githubPush()
        
        // Poll SCM every 5 minutes (backup if webhook fails)
        scm('H/5 * * * *')
        
        // Cron trigger for nightly builds
        cron('H 2 * * *') // Run at 2 AM daily
    }
    
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(repoUrl)
                        credentials(gitCredentialsId)
                    }
                    branch(gitBranch)
                    extensions {
                        // Clean workspace before checkout
                        cleanBeforeCheckout()
                        
                        // Clone with depth
                        cloneOptions {
                            shallow(false)
                            depth(1)
                            noTags(false)
                        }
                    }
                }
            }
            scriptPath('Jenkinsfile')
        }
    }
}

// Multibranch Pipeline Configuration (Alternative)
multibranchPipelineJob('simple_java_CRUD_S8_Multibranch') {
    description('Multibranch Pipeline for Travel Agency Management System')
    
    branchSources {
        github {
            id('simple_java_CRUD_S8')
            repoOwner('Btlguse')
            repository('simple_java_CRUD_S8')
            
            buildForkPRMerge(true)
            buildForkPRHead(true)
            buildOriginBranch(true)
            buildOriginBranchWithPR(true)
            buildOriginPRMerge(true)
            buildOriginPRHead(true)
        }
    }
    
    // Scan repository periodically
    triggers {
        periodic(1440) // Scan every 24 hours
    }
    
    orphanedItemStrategy {
        discardOldItems {
            numToKeep(10)
        }
    }
}

/**
 * Configuration for Jenkins Shared Libraries (if needed)
 */
@Library('shared-pipeline-library')
def sharedLib

/**
 * Global configuration settings
 * These should be configured in Jenkins > Manage Jenkins > Configure System
 */
println """
========================================
JENKINS CONFIGURATION CHECKLIST
========================================

1. CONFIGURE TOOLS (Manage Jenkins > Tools):
   - JDK 17:
     * Name: JDK-17
     * JAVA_HOME: Path to your JDK 17 installation
   
   - Maven:
     * Name: Maven-3.9
     * Version: 3.9.x or higher
     * Install automatically: YES

2. INSTALL REQUIRED PLUGINS:
   - Pipeline: https://plugins.jenkins.io/workflow-aggregator/
   - Git: https://plugins.jenkins.io/git/
   - GitHub: https://plugins.jenkins.io/github/
   - GitHub Branch Source: https://plugins.jenkins.io/github-branch-source/
   - JUnit: https://plugins.jenkins.io/junit/
   - JaCoCo: https://plugins.jenkins.io/jacoco/
   - Checkstyle: https://plugins.jenkins.io/checkstyle/
   - Email Extension: https://plugins.jenkins.io/email-ext/
   - HTML Publisher: https://plugins.jenkins.io/htmlpublisher/
   - Warnings Next Generation: https://plugins.jenkins.io/warnings-ng/
   - Job DSL: https://plugins.jenkins.io/job-dsl/

3. CONFIGURE CREDENTIALS (Manage Jenkins > Credentials):
   - GitHub credentials:
     * ID: github-credentials
     * Type: Username with password OR Personal Access Token
     * Description: GitHub access for repository
   
   - MySQL credentials:
     * ID: mysql-credentials
     * Type: Username with password
     * Username: your_mysql_user
     * Password: your_mysql_password

4. CONFIGURE EMAIL NOTIFICATIONS (Manage Jenkins > Configure System):
   - Extended E-mail Notification:
     * SMTP server: smtp.gmail.com (or your provider)
     * SMTP port: 587
     * Use SSL/TLS: YES
     * SMTP Authentication:
       - Username: your-email@example.com
       - Password: your-app-password
     * Default Recipients: your-email@example.com
     * Reply-To Address: noreply@yourcompany.com

5. CONFIGURE GITHUB WEBHOOK:
   - Go to GitHub Repository Settings > Webhooks
   - Add webhook:
     * Payload URL: http://your-jenkins-url/github-webhook/
     * Content type: application/json
     * Secret: (optional, recommended for security)
     * Events: Just the push event
     * Active: YES

6. CREATE MYSQL DATABASE:
   - Run the SQL script from README.md to create test_agency_g8 database
   - Ensure MySQL is running on localhost:3306
   - Grant necessary permissions to Jenkins MySQL user

7. CONFIGURE DEPLOYMENT DIRECTORY:
   - Create directory: C:\\Jenkins\\deployments\\proyecto_g8
   - Ensure Jenkins has write permissions

8. CONFIGURE JENKINSFILE:
   - Update EMAIL_RECIPIENTS in Jenkinsfile with your email
   - Update DEPLOY_DIR if you want a different deployment path
   - Update tool names (Maven-3.9, JDK-17) to match your Jenkins configuration

========================================
"""
