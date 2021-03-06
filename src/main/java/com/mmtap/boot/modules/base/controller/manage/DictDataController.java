package com.mmtap.boot.modules.base.controller.manage;

import com.mmtap.boot.common.utils.PageUtil;
import com.mmtap.boot.common.utils.ResultUtil;
import com.mmtap.boot.common.vo.PageVo;
import com.mmtap.boot.common.vo.Result;
import com.mmtap.boot.modules.base.entity.Dict;
import com.mmtap.boot.modules.base.entity.DictData;
import com.mmtap.boot.modules.base.service.DictDataService;
import com.mmtap.boot.modules.base.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Mmtap
 */
@Slf4j
@RestController
@Api(description = "字典数据管理接口")
@RequestMapping("/dictData")
@CacheConfig(cacheNames = "dictData")
@Transactional
public class DictDataController{

    @Autowired
    private DictService dictService;

    @Autowired
    private DictDataService dictDataService;

    @RequestMapping(value = "/getByCondition",method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result<Page<DictData>> getByCondition(@ModelAttribute DictData dictData,
                                                 @ModelAttribute PageVo pageVo){

        Page<DictData> page = dictDataService.findByCondition(dictData, PageUtil.initPage(pageVo));
        return new ResultUtil<Page<DictData>>().setData(page);
    }

    @RequestMapping(value = "/getByType/{type}",method = RequestMethod.GET)
    @ApiOperation(value = "通过类型获取")
    @Cacheable(key = "#type")
    public Result<Object> getByType(@PathVariable String type){

        Dict dict = dictService.findByType(type);
        if (dict == null) {
            return new ResultUtil<Object>().setErrorMsg("字典类型Type不存在");
        }
        List<DictData> list = dictDataService.findByDictId(dict.getId());
        return new ResultUtil<Object>().setData(list);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public Result<Object> add(@ModelAttribute DictData dictData){

        Dict dict = dictService.get(dictData.getDictId());
        if (dict == null) {
            return new ResultUtil<Object>().setErrorMsg("字典类型id不存在");
        }
        dictDataService.save(dictData);
        return new ResultUtil<Object>().setSuccessMsg("添加成功");
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public Result<Object> edit(@ModelAttribute DictData dictData){
        dictDataService.update(dictData);
        return new ResultUtil<Object>().setSuccessMsg("编辑成功");
    }

    @RequestMapping(value = "/delByIds/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delByIds(@PathVariable String[] ids){

        for(String id : ids){
            dictDataService.delete(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }
}
