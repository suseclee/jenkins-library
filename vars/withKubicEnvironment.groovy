// Copyright 2017 SUSE LINUX GmbH, Nuernberg, Germany.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
def call(Map parameters = [:], Closure body) {
    def nodeLabel = parameters.get('nodeLabel', 'devel')
    def gitBase = parameters.get('gitBase')
    def gitBranch = parameters.get('gitBranch')
    def gitCredentialsId = parameters.get('gitCredentialsId')

    echo "Creating Kubic Environment"

    // Allocate a node
    node (nodeLabel) {
        // Basic prep steps
        stage('Preparation') {
            step([$class: 'WsCleanup'])
            sh(script: 'mkdir logs')
        }

        // Fetch the necessary code
        stage('Retrieve Code') {
            cloneAllKubicRepos(gitBase: gitBase, branch: gitBranch, credentialsId: gitCredentialsId)
        }

        // Create the Kubic environment
        stage('Create Environment') {
            echo "TODO"
        }

        // Bootstrap the Kubic environment
        stage('Bootstrap Environment') {
            echo "TODO"
        }

        // Prepare the closure delegate
        def delegate = [:]
        // Set some context variables available inside the body() method
        //delegate['environment'] = environment
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body.delegate = delegate

        // Execute the body of the test
        body()

        // Gather logs from the environment
        stage('Gather Logs') {
            echo "TODO"
            sh(script: "touch logs/dummy.log")
        }

        // Destroy the Kubic Environment
        stage('Destroy Environment') {
            echo "TODO"
        }

        // Archive the logs
        stage('Archive Logs') {
            archiveArtifacts(artifacts: 'logs/*', fingerprint: true)
        }
    }

}
