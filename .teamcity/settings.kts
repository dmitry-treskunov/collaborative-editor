import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2019.1"

project {

    buildType(Build)

    features {
        feature {
            id = "PROJECT_EXT_271"
            type = "Invitation"
            param("createdByUserId", "3190")
            param("invitationType", "joinProjectInvitation")
            param("secure:token", "credentialsJSON:8c183005-09c7-4dc7-9dbb-94a0b9e18998")
            param("name", "Join TeamCity TestDrive")
            param("welcomeText", "Dmitry Treskunov invites you to join Collaborative Editor (3) project.")
            param("disabled", "false")
            param("groupKey", "TD_DEVS_39553")
            param("multi", "true")
        }
    }
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
            jdkHome = "%env.JDK_17%"
        }
    }

    triggers {
        vcs {
        }
    }
})
