pipeline {
    //def mvnHome
    agent {
        //docker { image 'tiangolo/docker-with-compose' }
	docker {
            image 'maven-docker-compose:latest'
            args '-u root -v /var/jenkins_home/.m2:/root/.m2'
            //args '-v "$PWD":/usr/src/mymaven -v "$HOME/.m2":/root/.m2 -v "$PWD/target:/usr/src/mymaven/target"'
        }
    }
    //tools { 
        //maven 'M3'
        //jdk 'jdk8' 
    //}
  stages{
      stage('Copying2'){   
        steps{
          sh 'rm -rf $HOME/orchestrator'
          sh 'cp -R /mnt/share/orchestrator $HOME/orchestrator'
        }
      }
      stage('Build') {
          steps{
             sh 'mvn -version'
             sh 'docker-compose -version'  
             //sh 'sleep 3600'
             //sh 'cd $HOME/orchestrator/fiware && make install'
          }
      }
      stage('Tests') {
          steps{
             sh 'cd $HOME/orchestrator/fiware/clients && sh make.sh compose-up'
             sh 'cd $HOME/orchestrator/fiware/clients && mvn -Dtest=NgsiLDClientTest test'
             sh 'cd $HOME/orchestrator/fiware/clients && sh make.sh compose-down'
          }
     }
      
      //stage('Results') {
      //    steps{
      //      junit '**/target/surefire-reports/TEST-*.xml'
       //     //archiveArtifacts 'target/*.jar'
       //   }
       //}
  }
}

