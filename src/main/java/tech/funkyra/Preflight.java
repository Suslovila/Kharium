package tech.funkyra;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.CoreModManager;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

public class Preflight {
    public static void boot() {
        List<File> mods = Lists.newArrayList();
        mods.addAll(getMods());
        mods.forEach(mod -> {
            try {
                if (!mod.getName().contains(MixedFixes.NAME)) {
                    addCandidate(mod);
                }
            } catch (MalformedURLException ignored) {
            }
        });
    }

    private static List<File> getMods() {
        File folder = new File("mods");
        List<File> mods = Lists.newArrayList();
        if (folder.exists() && folder.isDirectory() && folder.listFiles() != null) {
            File[] jars = folder.listFiles(f -> (f.getName().endsWith(".jar") || f.isDirectory()));
            if (jars != null)
                mods.addAll(Arrays.asList(jars));
        }
        return mods;
    }

    private static void addCandidate(File mod) throws MalformedURLException {
        ((LaunchClassLoader) MixinPreflight.class.getClassLoader()).addURL(mod.toURI().toURL());
        CoreModManager.getReparseableCoremods().add(mod.getName());
    }
}
