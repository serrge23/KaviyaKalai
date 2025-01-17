/***********************************************************************************************************************
 *  DEV ENABLEMENT - JENKINS PIPELINE OFFERING - Version 3.1.0
 *  NOTE:
 *      1. CF space(s), environment values, and properties are defined in 'pipeline/pipeline.configuration.groovy' file
 *      2. Pipeline helper functions are defined in 'pipeline/pipeline.helper.groovy' file
 **********************************************************************************************************************/

def cfEnvironment = 'DEV' // Replace value with environment key from pipeline.configuration.groovy (e.g., DEV, QA, PROD)
def _helper

def nodeLabel = ''  //Replace the nodeLabel with a slave node label or leave it empty for master node.


timeout(time: 20, unit: 'MINUTES') {
    node(nodeLabel) {
        stage('Pipeline Setup') {
            deleteDir()
            checkout scm
            _helper = load 'pipeline/pipeline.helper.groovy'
            _helper.initialize("${cfEnvironment}")
        }

        if (_helper.canDownloadMyPreviouslyBuiltArtifact()) {
            stage('Download Artifact from Nexus') {
                sh './gradlew downloadMyPreviouslyBuiltArtifact'
            }
        } else {
            stage('Build') {
                sh './gradlew pipelineBuild'
            }

            if (_helper.property('integrations.thirdParty.checkmarx.enabled')) {
                stage("Quality Check (Checkmarx)") {
                    _helper.runCheckmarx()
                    if (currentBuild.result == "FAILURE") sh 'exit 1'
                }
            }

            if (_helper.property('integrations.thirdParty.sonarQube.enabled')) {
                stage("Quality Check (SonarQube)") {
                    if (_helper.property('integrations.thirdParty.sonarQube.enableBreakBuildOnIssue')) {
                        withSonarQubeEnv() { sh './gradlew pipelineQualityCheck' }
                        if (waitForQualityGate().status != 'OK') error "Pipeline aborted due to quality gate failure."
                    } else {
                        sh './gradlew pipelineQualityCheck'
                    }
                }
            }

            if (_helper.property('integrations.thirdParty.fossa.enabled')) {
                stage("OSS License Check (FOSSA)") {
                    sh './gradlew fossaScan'
                }
            }
        }

        if (env.BRANCH_NAME == _helper.propertyCf('integrations.thirdParty.gitHub.branchName')) {
            _helper.stageForEachCfSpace('Blue: Stage w/ Tests (%s)') {
                _helper.executePipelineCfServicesScript()
                sh './gradlew cfManifest pipelineCfStage acceptanceTests'
            }

            if (_helper.canPublish()) {
                stage("Publish (Nexus)") {
                    sh './gradlew pipelinePublish'
                }
            }

            _helper.stageForEachCfSpace(_helper.getReleaseStageLabelText()) {
                if (_helper.propertyCf('c2cNetworkPolicies.enabled')) sh './gradlew pipelineCfAddNetworkPolicies'
                sh './gradlew pipelineCfRelease'
            }
        }

        _helper.cleanUp()
    }
}
