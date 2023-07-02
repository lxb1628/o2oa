package com.x.server.console.action;

import java.util.Objects;

import com.x.base.core.entity.annotation.ContainerEntity;
import com.x.base.core.entity.dataitem.ItemCategory;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

public class EraseContentCms extends EraseContent {

    private static final Logger LOGGER = LoggerFactory.getLogger(EraseContentCms.class);

    @Override
    public boolean execute() {
        try {
            ClassLoader classLoader = EntityClassLoaderTools.concreteClassLoader();
            Thread.currentThread().setContextClassLoader(classLoader);
            this.init("cms", ItemCategory.cms, classLoader);
            try (ScanResult sr = new ClassGraph().addClassLoader(classLoader)
                    .acceptPackages("com.x.cms.core.entity")
                    .enableAnnotationInfo().scan()) {
                for (ClassInfo info : sr.getClassesWithAnnotation(ContainerEntity.class.getName())) {
                    Class<?> cls = classLoader.loadClass(info.getName());
                    ContainerEntity containerEntity = cls.getAnnotation(ContainerEntity.class);
                    if (Objects.equals(containerEntity.type(), ContainerEntity.Type.content)
                            || Objects.equals(containerEntity.type(), ContainerEntity.Type.log)) {
                        addClass(cls);
                    }
                }
            }
            addClass(classLoader.loadClass("com.x.query.core.entity.Item"));
//			addClass(classLoader.loadClass("com.x.cms.core.entity.CmsBatchOperation"));
//			addClass(classLoader.loadClass("com.x.cms.core.entity.Document"));
//			addClass(classLoader.loadClass("com.x.cms.core.entity.DocumentCommend"));
//			addClass(classLoader.loadClass("com.x.cms.core.entity.DocumentCommentCommend"));
//			addClass(classLoader.loadClass("com.x.cms.core.entity.DocumentCommentContent"));
//			addClass(classLoader.loadClass("com.x.cms.core.entity.DocumentCommentInfo"));
//			addClass(classLoader.loadClass("com.x.cms.core.entity.DocumentViewRecord"));
//			addClass(classLoader.loadClass("com.x.cms.core.entity.FileInfo"));
//			addClass(classLoader.loadClass("com.x.cms.core.entity.Log"));
//			addClass(classLoader.loadClass("com.x.cms.core.entity.ReadRemind"));
//			addClass(classLoader.loadClass("com.x.cms.core.entity.Review"));
//			addClass(classLoader.loadClass("com.x.query.core.entity.Item"));
            this.run();
            return true;
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return false;
    }
}