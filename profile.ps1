git checkout no_ksp
./gradlew clean
gradle-profiler --benchmark --iterations 100 --project-dir . --scenario-file profile-scenarios.txt

git checkout main
./gradlew clean
gradle-profiler --benchmark --iterations 100 --project-dir . --scenario-file profile-scenarios.txt
