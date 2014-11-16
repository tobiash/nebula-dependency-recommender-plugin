package netflix.nebula.dependency.recommendations.provider;

import aQute.bnd.maven.support.Maven;
import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.Namer;
import org.gradle.api.Project;
import org.gradle.api.internal.ClosureBackedAction;
import org.gradle.api.internal.ConfigureByMapAction;
import org.gradle.api.internal.DefaultNamedDomainObjectList;

import java.util.HashMap;
import java.util.Map;

public class RecommendationProviderContainer extends DefaultNamedDomainObjectList<RecommendationProvider> {
    private Project project;

    private final Action<? super RecommendationProvider> addLastAction = new Action<RecommendationProvider>() {
        public void execute(RecommendationProvider r) {
            RecommendationProviderContainer.super.add(r);
        }
    };

    public RecommendationProviderContainer(Project project) {
        super(RecommendationProvider.class, null, new RecommendationProviderNamer());
        this.project = project;
    }

    private static class RecommendationProviderNamer implements Namer<RecommendationProvider> {
        public String determineName(RecommendationProvider r) {
            return r.getName();
        }
    }

    public <T extends RecommendationProvider> T add(T provider, Action<? super T> configureAction) {
        configureAction.execute(provider);
        assertCanAdd(provider.getName());
        addLastAction.execute(provider);
        return provider;
    }

    public PropertyFileRecommendationProvider propertiesFile(Map<String, ?> args) {
        Map<String, Object> modifiedArgs = new HashMap<String, Object>(args);
        return add(new PropertyFileRecommendationProvider(project), new ConfigureByMapAction<PropertyFileRecommendationProvider>(modifiedArgs));
    }

    public PropertyFileRecommendationProvider propertiesFile(Closure closure) {
        return add(new PropertyFileRecommendationProvider(project), new ClosureBackedAction<PropertyFileRecommendationProvider>(closure));
    }

    public MavenBomRecommendationProvider mavenBom(Map<String, ?> args) {
        Map<String, Object> modifiedArgs = new HashMap<String, Object>(args);
        return add(new MavenBomRecommendationProvider(project), new ConfigureByMapAction<MavenBomRecommendationProvider>(modifiedArgs));
    }

    public MavenBomRecommendationProvider mavenBom(Closure closure) {
        return add(new MavenBomRecommendationProvider(project), new ClosureBackedAction<MavenBomRecommendationProvider>(closure));
    }

    public DependencyLockProvider dependencyLock(Map<String, ?> args) {
        Map<String, Object> modifiedArgs = new HashMap<String, Object>(args);
        return add(new DependencyLockProvider(project), new ConfigureByMapAction<DependencyLockProvider>(modifiedArgs));
    }

    public DependencyLockProvider dependencyLock(Closure closure) {
        return add(new DependencyLockProvider(project), new ClosureBackedAction<DependencyLockProvider>(closure));
    }

    public MapRecommendationProvider map(Map<String, ?> args) {
        Map<String, Object> modifiedArgs = new HashMap<String, Object>(args);
        return add(new MapRecommendationProvider(), new ConfigureByMapAction<MapRecommendationProvider>(modifiedArgs));
    }

    public MapRecommendationProvider map(Closure closure) {
        return add(new MapRecommendationProvider(), new ClosureBackedAction<MapRecommendationProvider>(closure));
    }

    public CustomRecommendationProvider add(Closure closure) {
        return add(new CustomRecommendationProvider(closure), new Action<CustomRecommendationProvider>() {
            @Override
            public void execute(CustomRecommendationProvider customRecommendationProvider) {
            }
        });
    }
}
