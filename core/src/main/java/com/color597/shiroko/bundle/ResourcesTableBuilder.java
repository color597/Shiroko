package com.color597.shiroko.bundle;

import com.android.aapt.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import androidx.annotation.NonNull;


/**
 * To generate {@link com.android.aapt.Resources.ResourceTable} class
 * <p>
 * Created by YangJing on 2019/04/15 .
 * Email: yangjing.yeoh@bytedance.com
 */
public class ResourcesTableBuilder {

    private final Resources.ResourceTable.Builder table;
    private final Map<String, PackageBuilder> resPackageMap;
    private final List<Resources.Package> resPackages;


    public ResourcesTableBuilder() {
        table = Resources.ResourceTable.newBuilder();
        resPackageMap = new HashMap<>();
        resPackages = new ArrayList<>();
    }

    /**
     * 添加 package
     */
    public PackageBuilder addPackage(Resources.Package resPackage) {
        if (resPackageMap.containsKey(resPackage.getPackageName())) {
            return resPackageMap.get(resPackage.getPackageName());
        }
        PackageBuilder packageBuilder = new PackageBuilder(resPackage);
        resPackageMap.put(resPackage.getPackageName(), packageBuilder);
        return packageBuilder;
    }

    /**
     * 生成 ResourceTable
     */
    public Resources.ResourceTable build() {
        resPackageMap.forEach((key, value) -> table.addPackage(value.resPackageBuilder.build()));
        return table.build();
    }

    public class PackageBuilder {

        Resources.Package.Builder resPackageBuilder;

        private PackageBuilder(Resources.Package resPackage) {
            addPackage(resPackage);
        }

        public ResourcesTableBuilder build() {
//            resPackages.add(resPackageBuilder.build());
            return ResourcesTableBuilder.this;
        }

        /**
         * 添加 package
         */
        private void addPackage(Resources.Package resPackage) {
            int id = resPackage.getPackageId().getId();
            checkArgument(
                    table.getPackageList().stream().noneMatch(pkg -> pkg.getPackageId().getId() == id),
                    "Package ID %s already in use.",
                    id);

            resPackageBuilder = Resources.Package.newBuilder()
                    .setPackageId(resPackage.getPackageId())
                    .setPackageName(resPackage.getPackageName());
        }

        /**
         * From current package find type, if not exist, then add into package
         */
        @NonNull
        Resources.Type.Builder getResourceType(@NonNull Resources.Type resType) {
            return resPackageBuilder.getTypeBuilderList().stream()
                    .filter(type -> type.getTypeId().getId() == resType.getTypeId().getId())
                    .findFirst()
                    .orElseGet(() -> addResourceType(resType));
        }

        @NonNull
        Resources.Type.Builder addResourceType(@NonNull Resources.Type resType) {
            Resources.Type.Builder typeBuilder = Resources.Type.newBuilder()
                    .setName(resType.getName())
                    .setTypeId(resType.getTypeId());
            resPackageBuilder.addType(typeBuilder);
            return getResourceType(resType);
        }

        /**
         * Add resource
         * <p>
         * If Entry don't specify  id , The resource will not add into ResourceTable, force specify the id as 0 here.
         *
         * @param resType  resource type
         * @param resEntry entry
         */
        @SuppressWarnings("UnusedReturnValue")
        public PackageBuilder addResource(@NonNull Resources.Type resType,
                                          @NonNull Resources.Entry resEntry) {
            // 如果 package 中不存在 package 则先添加 type
            Resources.Type.Builder type = getResourceType(resType);
            // 添加 entry 资源
            checkState(resPackageBuilder != null, "A package must be created before a resource can be added.");
            if (!resEntry.getEntryId().isInitialized()) {
                resEntry = resEntry.toBuilder().setEntryId(
                        resEntry.getEntryId().toBuilder().setId(0).build()
                ).build();
            }
            type.addEntry(resEntry.toBuilder());
            return this;
        }
    }
}
