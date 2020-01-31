package com.scy.apidemo.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.ib.client.Contract;
import com.scy.rx.model.ContractModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SelectedContractsConf {
    public static List<Contract> getContracts(String path) {
        try {
            File file = new File(path);
            String content = FileUtils.readFileToString(file, "UTF-8");
            List<ContractModel> contractModels = JSON.parseObject(content, new TypeReference<List<ContractModel>>() {
            });
            if (contractModels == null) {
                return Lists.newArrayListWithCapacity(0);
            }
            return contractModels.stream().map(ContractModel::toContract).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("SelectedContractsConf.getContracts failed.", e);
            throw new RuntimeException(e);
        }
    }


}
