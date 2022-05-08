# execdoc

- the consolidated version of the other /execdoc/ projects

Maybe add in .m2/settings.xml

    <profiles>
      <profile>
         <id>allow-snapshots</id>
            <activation><activeByDefault>true</activeByDefault></activation>
         <repositories>
           <repository>
             <id>snapshots-repo-1</id>
             <url>https://oss.sonatype.org/content/repositories/snapshots</url>
             <releases><enabled>false</enabled></releases>
             <snapshots><enabled>true</enabled></snapshots>
           </repository>
           <repository>
             <id>snapshots-repo-2</id>
             <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
             <releases><enabled>false</enabled></releases>
             <snapshots><enabled>true</enabled></snapshots>
           </repository>
         </repositories>
       </profile>
    </profiles>