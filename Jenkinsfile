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



def GIT_BRANCH_BASE = env.BRANCH_NAME.split('/')



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
        stage('Build') {
            steps {
                echo 'Running Build'
                script {

                    sh "./gradlew build"
                    
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Copying to web server'

                echo GIT_BRANCH_BASE[0] + env.BUILD_NUMBER
                
                echo env.BUILD_NUMBER

                
                script {
                    sh "ls"
                }
            }
        }
  
    }
}
