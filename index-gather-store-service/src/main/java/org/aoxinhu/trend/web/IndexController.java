package org.aoxinhu.trend.web;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
 
import org.aoxinhu.trend.pojo.Index;
import org.aoxinhu.trend.service.IndexService;
  
@RestController
public class IndexController {
    @Autowired IndexService indexService;
 
    // @GetMapping("/getCodes")
    // public List<Index> get() throws Exception {
    //     return indexService.fetch_indexes_from_third_part();
    // }

    @GetMapping("/freshCodes")
    public List<Index> fresh() throws Exception {
        return indexService.fresh();
    }
    @GetMapping("/getCodes")
    public List<Index> get() throws Exception {
        return indexService.get();
    }
    @GetMapping("/removeCodes")
    public String remove() throws Exception {
        indexService.remove();
        return "remove codes successfully";
    }
}