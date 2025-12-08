# Jenkins CI/CD Setup Guide
# Travel Agency Management System - Java CRUD Application

## 📋 Table of Contents
1. [Prerequisites](#prerequisites)
2. [Jenkins Installation](#jenkins-installation)
3. [Jenkins Configuration](#jenkins-configuration)
4. [GitHub Webhook Setup](#github-webhook-setup)
5. [Email Notifications Setup](#email-notifications-setup)
6. [Running the Pipeline](#running-the-pipeline)
7. [Troubleshooting](#troubleshooting)

---

## 🔧 Prerequisites

Before setting up Jenkins, ensure you have:

### 1. Java Development Kit (JDK) 17
```powershell
# Verify Java installation
java -version

# Should output: java version "17.x.x"
```
**Download:** https://adoptium.net/

### 2. Maven 3.9+
```powershell
# Verify Maven installation
mvn -version

# Should output: Apache Maven 3.9.x
```
**Download:** https://maven.apache.org/download.cgi

### 3. MySQL Database
```powershell
# Verify MySQL is running
mysql -u root -p -e "SELECT VERSION();"
```
**Download:** https://dev.mysql.com/downloads/mysql/

### 4. Git
```powershell
# Verify Git installation
git --version
```
**Download:** https://git-scm.com/download/win

---

## 🚀 Jenkins Installation

### Step 1: Download Jenkins
1. Go to https://www.jenkins.io/download/
2. Download the Windows installer (`.msi` file) or the generic `.war` file

### Step 2: Install Jenkins (Windows Installer)
```powershell
# Run the installer and follow the wizard
# Default installation path: C:\Program Files\Jenkins

# Jenkins will start automatically on: http://localhost:8080
```

### Step 3: Initial Setup
1. Open browser: `http://localhost:8080`
2. Unlock Jenkins using the initial admin password:
   ```powershell
   # Find the password at:
   Get-Content "C:\Program Files\Jenkins\secrets\initialAdminPassword"
   ```
3. Select "Install suggested plugins"
4. Create your first admin user
5. Configure Jenkins URL: `http://localhost:8080`

---

## ⚙️ Jenkins Configuration

### Step 1: Install Required Plugins

Go to **Manage Jenkins > Plugins > Available plugins** and install:

- ✅ **Pipeline** - Pipeline functionality
- ✅ **Git** - Git SCM support
- ✅ **GitHub** - GitHub integration
- ✅ **GitHub Branch Source** - Multibranch pipeline support
- ✅ **JUnit** - Test result visualization
- ✅ **Email Extension** - Advanced email notifications
- ✅ **HTML Publisher** - HTML report publishing
- ✅ **Warnings Next Generation** - Static analysis visualization
- ✅ **Job DSL** - Pipeline job configuration
- ✅ **Config File Provider** - Configuration file management

After installation, restart Jenkins:
```powershell
# Restart Jenkins service
Restart-Service Jenkins
```

### Step 2: Configure Tools

#### Configure JDK
1. Go to **Manage Jenkins > Tools**
2. Scroll to **JDK installations**
3. Click **Add JDK**
   - Name: `JDK-17`
   - Uncheck "Install automatically"
   - JAVA_HOME: `C:\Program Files\Java\jdk-17` (or your JDK path)

#### Configure Maven
1. Scroll to **Maven installations**
2. Click **Add Maven**
   - Name: `Maven-3.9`
   - Check "Install automatically"
   - Version: Select Maven 3.9.x or higher

#### Configure Git
1. Scroll to **Git installations**
2. Git should be auto-detected
   - Name: `Default`
   - Path to Git executable: `C:\Program Files\Git\bin\git.exe`

### Step 3: Configure Credentials

#### GitHub Credentials
1. Go to **Manage Jenkins > Credentials**
2. Click **(global)** domain
3. Click **Add Credentials**
   - Kind: `Username with password` or `Secret text` (for Personal Access Token)
   - ID: `github-credentials`
   - Username: Your GitHub username
   - Password: Your GitHub Personal Access Token
   - Description: `GitHub access for repository`

**To create GitHub Personal Access Token:**
1. Go to GitHub Settings > Developer settings > Personal access tokens
2. Generate new token (classic)
3. Select scopes: `repo`, `admin:repo_hook`, `admin:org_hook`
4. Copy the token and use it as password

#### MySQL Credentials
1. Click **Add Credentials** again
   - Kind: `Username with password`
   - ID: `mysql-credentials`
   - Username: Your MySQL username (e.g., `root`)
   - Password: Your MySQL password
   - Description: `MySQL database credentials`

### Step 4: Setup MySQL Database

Run the SQL script to create the test database:

```sql
CREATE DATABASE travel_agency_g8;
USE travel_agency_g8;

-- Cliente table
CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(200)
);

-- Reserva table
CREATE TABLE reserva (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    fecha_reserva DATE NOT NULL,
    destino VARCHAR(100) NOT NULL,
    fecha_viaje DATE NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    estado ENUM('PENDIENTE', 'CONFIRMADA', 'CANCELADA') DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

-- Factura table
CREATE TABLE factura (
    id_factura INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT NOT NULL,
    fecha_emision DATE NOT NULL,
    monto_total DECIMAL(10,2) NOT NULL,
    estado ENUM('PENDIENTE', 'PAGADA', 'ANULADA') DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_reserva) REFERENCES reserva(id_reserva)
);
```

**Grant permissions:**
```sql
GRANT ALL PRIVILEGES ON travel_agency_g8.* TO 'your_user'@'localhost';
FLUSH PRIVILEGES;
```

### Step 5: Create Deployment Directory

```powershell
# Create deployment directory
New-Item -Path "C:\Jenkins\deployments\proyecto_g8" -ItemType Directory -Force

# Verify directory exists
Test-Path "C:\Jenkins\deployments\proyecto_g8"
```

---

## 🔗 GitHub Webhook Setup

### Step 1: Configure Webhook in GitHub

1. Go to your GitHub repository
2. Navigate to **Settings > Webhooks**
3. Click **Add webhook**
4. Configure:
   - **Payload URL:** `http://your-jenkins-url:8080/github-webhook/`
     - For local testing with public access, use ngrok or similar tool
   - **Content type:** `application/json`
   - **Secret:** (optional, but recommended)
   - **Which events:** Select "Just the push event"
   - **Active:** ✅ Check this box
5. Click **Add webhook**

### Step 2: Test Webhook

1. Make a small commit to your repository
2. Check webhook delivery in GitHub: Settings > Webhooks > Recent Deliveries
3. Verify the response is `200 OK`

### Alternative: Use SCM Polling

If webhooks don't work (local Jenkins without public access):

The Jenkinsfile already includes SCM polling:
```groovy
triggers {
    scm('H/5 * * * *') // Poll every 5 minutes
}
```

---

## 📧 Email Notifications Setup

### Step 1: Configure Email Extension Plugin

1. Go to **Manage Jenkins > System**
2. Scroll to **Extended E-mail Notification**

#### For Gmail:
- **SMTP server:** `smtp.gmail.com`
- **SMTP port:** `587`
- **Use SSL:** No
- **Use TLS:** Yes
- Click **Advanced**
  - **SMTP Username:** `your-email@gmail.com`
  - **SMTP Password:** Use App Password (not your Gmail password)
  - **Charset:** `UTF-8`
- **Default Recipients:** `your-email@gmail.com`
- **Reply-To Address:** `noreply@yourcompany.com`

**To generate Gmail App Password:**
1. Enable 2-Factor Authentication on Gmail
2. Go to Google Account > Security > App passwords
3. Generate password for "Mail" and "Windows Computer"
4. Use this password in Jenkins

#### For Outlook/Hotmail:
- **SMTP server:** `smtp-mail.outlook.com`
- **SMTP port:** `587`
- **Use TLS:** Yes

#### For Custom SMTP:
Configure according to your email provider's settings

### Step 2: Update Jenkinsfile

Update the email in `Jenkinsfile`:
```groovy
environment {
    EMAIL_RECIPIENTS = 'your-email@example.com' // Change this!
}
```

### Step 3: Test Email Notification

```groovy
// Add test stage in Jenkinsfile temporarily
stage('Test Email') {
    steps {
        emailext(
            subject: "Test Email from Jenkins",
            body: "This is a test email.",
            to: "your-email@example.com"
        )
    }
}
```

---

## 🎯 Running the Pipeline

### Step 1: Create Jenkins Job

#### Option A: Pipeline Job (Recommended)
1. Click **New Item**
2. Enter name: `simple_java_CRUD_S8_Pipeline`
3. Select **Pipeline**
4. Click **OK**
5. Configure:
   - **Description:** CI/CD Pipeline for Travel Agency Management System
   - **Build Triggers:**
     - ✅ GitHub hook trigger for GITScm polling
     - ✅ Poll SCM: `H/5 * * * *`
   - **Pipeline:**
     - Definition: `Pipeline script from SCM`
     - SCM: `Git`
     - Repository URL: `https://github.com/Btlguse/simple_java_CRUD_S8.git`
     - Credentials: Select `github-credentials`
     - Branch Specifier: `*/feature/JAVIER` (or your branch)
     - Script Path: `Jenkinsfile`
6. Click **Save**

#### Option B: Multibranch Pipeline
1. Click **New Item**
2. Enter name: `simple_java_CRUD_S8_Multibranch`
3. Select **Multibranch Pipeline**
4. Click **OK**
5. Configure:
   - **Branch Sources > Add source > GitHub**
     - Credentials: `github-credentials`
     - Repository URL: `https://github.com/Btlguse/simple_java_CRUD_S8.git`
   - **Build Configuration:**
     - Mode: `by Jenkinsfile`
     - Script Path: `Jenkinsfile`
6. Click **Save**

### Step 2: Run the Pipeline

#### Manual Trigger:
1. Go to your pipeline job
2. Click **Build Now**
3. Watch the pipeline progress in **Stage View**
4. Click on stages to see logs

#### Automatic Trigger (Webhook):
1. Make a commit to your repository
2. Push to GitHub
3. Jenkins will automatically trigger the build

### Step 3: View Results

After build completes:
- **Console Output:** Full build logs
- **Test Results:** JUnit test report with pass/fail stats
- **Code Coverage:** JaCoCo report showing % coverage
- **Checkstyle Report:** Code quality issues
- **Maven Site Report:** Comprehensive project documentation
- **Artifacts:** Download built JAR files

### Step 4: Check Deployment

```powershell
# Verify deployment
Get-ChildItem "C:\Jenkins\deployments\proyecto_g8"

# Should show:
# - proyecto_g8-{BUILD_NUMBER}.jar
# - proyecto_g8-latest.jar
```

### Step 5: Run Deployed Application

```powershell
# Navigate to deployment directory
cd "C:\Jenkins\deployments\proyecto_g8"

# Run the latest version
java -jar proyecto_g8-latest.jar
```

---

## 🐛 Troubleshooting

### Issue 1: Build Fails - "Cannot find Maven"
**Solution:**
```powershell
# Verify Maven is configured in Jenkins
# Go to Manage Jenkins > Tools > Maven installations
# Make sure the name matches "Maven-3.9" in Jenkinsfile
```

### Issue 2: Tests Fail - "Cannot connect to MySQL"
**Solution:**
1. Check MySQL is running:
   ```powershell
   Get-Service -Name MySQL*
   ```
2. Verify database exists:
   ```sql
   SHOW DATABASES LIKE 'test_agency_g8';
   ```
3. Update credentials in `src/main/java/util/ConexionMySQL.java`

### Issue 3: GitHub Webhook Not Triggering
**Solution:**
1. Check webhook deliveries in GitHub
2. Verify Jenkins is accessible from internet (use ngrok for local)
3. Use SCM polling as alternative: `H/5 * * * *`

### Issue 4: Email Notifications Not Working
**Solution:**
1. Test SMTP connection:
   ```powershell
   # In Jenkins Script Console (Manage Jenkins > Script Console)
   ```
2. Verify email credentials
3. Check spam folder
4. Enable "Less secure app access" or use App Password

### Issue 5: Permission Denied on Deployment Directory
**Solution:**
```powershell
# Grant permissions to Jenkins service account
icacls "C:\Jenkins\deployments\proyecto_g8" /grant "NT SERVICE\Jenkins:(OI)(CI)F" /T
```

### Issue 6: Build Times Out
**Solution:**
1. Increase timeout in Jenkinsfile:
   ```groovy
   options {
       timeout(time: 30, unit: 'MINUTES')
   }
   ```

### Issue 7: JaCoCo Report Not Showing
**Solution:**
1. Verify JaCoCo plugin is installed
2. Check that tests are running: `mvn test`
3. Verify `target/jacoco.exec` exists after build

---

## 📊 Pipeline Stages Explained

### 1. **Checkout**
- Clones code from GitHub repository
- Switches to configured branch

### 2. **Build**
- Compiles Java source code
- Command: `mvn clean compile`

### 3. **Test**
- Runs all unit tests
- Generates JUnit test reports
- Generates JaCoCo code coverage reports
- Command: `mvn test`

### 4. **Code Quality Analysis**
- Runs Checkstyle for code style violations
- Generates static analysis reports
- Command: `mvn checkstyle:checkstyle`

### 5. **Package**
- Creates executable JAR files
- Includes dependencies in JAR
- Archives artifacts in Jenkins
- Command: `mvn package -DskipTests`

### 6. **Deploy to Test Environment**
- Copies JAR to deployment directory
- Creates versioned copy (with build number)
- Creates latest symlink
- Destination: `C:\Jenkins\deployments\proyecto_g8`

### 7. **Generate Reports**
- Creates Maven site documentation
- Publishes HTML reports
- Command: `mvn site`

---

## 🔄 Maintenance

### Update Dependencies
```powershell
# Check for dependency updates
mvn versions:display-dependency-updates

# Update dependencies
mvn versions:use-latest-versions
```

### Clean Old Builds
Jenkins automatically keeps last 10 builds (configured in pipeline)

### Backup Jenkins Configuration
```powershell
# Backup Jenkins home directory
Copy-Item -Path "C:\Program Files\Jenkins" -Destination "C:\Backup\Jenkins_$(Get-Date -Format 'yyyy-MM-dd')" -Recurse
```

---

## 📚 Additional Resources

- **Jenkins Documentation:** https://www.jenkins.io/doc/
- **Pipeline Syntax:** https://www.jenkins.io/doc/book/pipeline/syntax/
- **Maven Documentation:** https://maven.apache.org/guides/
- **JaCoCo Documentation:** https://www.jacoco.org/jacoco/trunk/doc/
- **GitHub Webhooks:** https://docs.github.com/en/webhooks

---

## ✅ Quick Checklist

Before running pipeline:
- [ ] Jenkins installed and running
- [ ] All required plugins installed
- [ ] JDK 17 configured
- [ ] Maven configured
- [ ] GitHub credentials added
- [ ] MySQL credentials added
- [ ] MySQL database created
- [ ] Deployment directory created
- [ ] GitHub webhook configured
- [ ] Email notifications configured
- [ ] Jenkinsfile email updated
- [ ] Pipeline job created
- [ ] First build triggered successfully

---

**Created:** December 8, 2025  
**Project:** simple_java_CRUD_S8  
**Branch:** feature/JAVIER  
**Author:** GitHub Copilot
