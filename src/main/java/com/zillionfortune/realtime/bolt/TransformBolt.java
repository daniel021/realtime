package com.zillionfortune.realtime.bolt;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zillionfortune.realtime.model.Ywlog;
import com.zillionfortune.realtime.util.YwlogParse;

public class TransformBolt extends BaseBasicBolt{
	
   // private static Logger LOG = LoggerFactory.getLogger(TransformBolt.class);
	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		//过滤垃圾日志
		//String line = new String((byte[]) tuple.getValue(0), "UTF-8");
		String line = tuple.getValue(0).toString();
		try {
			line = new String(line.getBytes(),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//过滤规则， 不是"{"开头的都不去做parse
		if(!line.startsWith("{")){
			//什么也不处理
			System.out.println("Log format content[SKIPPED]:" + line);
		}else{
			//logParse
			YwlogParse parse = new YwlogParse();
			Ywlog logmodel = parse.logParse(line);
			//发送ywlog对象
			if(logmodel.getParse()){
				collector.emit(new Values(logmodel));
			}
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("ywlog")); 
	}
	

	

}
