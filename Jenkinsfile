class Constants {

    static final String MASTER_BRANCH = 'master'

    static final String QA_BUILD = 'Debug'
    static final String RELEASE_BUILD = 'Release'

    static final String INTERNAL_TRACK = 'internal'
    static final String RELEASE_TRACK = 'release'
}

def getBuildType() {
    switch (env.BRANCH_NAME) {
        case Constants.MASTER_BRANCH:
            return Constants.RELEASE_BUILD
        default:
            return Constants.QA_BUILD
    }
}

def getTrackType() {
    switch (env.BRANCH_NAME) {
        case Constants.MASTER_BRANCH:
            return Constants.RELEASE_TRACK
        default:
            return Constants.INTERNAL_TRACK
    }
}

def isDeployCandidate() {
    return ("${env.BRANCH_NAME}" =~ /(develop|master)/)
}

pipeline {
    agent { 
        docker {
            image 'mingc/android-build-box:latest' 
            args  '-v android-sdk-cache:/opt/android-sdk/'
                }
            }
    environment {
        appName = 'wig'

    }
    stages {
        stage('Run Tests') {
            steps {
                echo 'Running Tests'
                script {

                    sh "./gradlew build"

                    ls -la
                    
                }
            }
        }
        stage('Build Bundle') {
            when { expression { return isDeployCandidate() } }
            steps {
                echo 'Building'
                script {
                    sh "./gradlew bundle${env.BRANCH_NAME}"
                }
            }
        }
  
    }
}
