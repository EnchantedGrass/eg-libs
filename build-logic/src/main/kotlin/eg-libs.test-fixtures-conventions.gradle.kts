plugins {
    `java-test-fixtures`
}

sourceSets {
    testFixtures {
        java.setSrcDirs(listOf("testFixtures"))
        resources.setSrcDirs(listOf("testFixturesResources"))
        (extensions.findByName("kotlin") as? SourceDirectorySet)?.setSrcDirs(listOf("testFixtures"))
    }
}
