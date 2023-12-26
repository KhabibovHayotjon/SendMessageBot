package send.bot.message.repository;

import org.hibernate.grammars.hql.HqlParser;
import send.bot.message.entitiy.TypeItems;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TypeItemRepository {
    private Map<Long, List<TypeItems>> listMap = new HashMap<>();
    public int add(Long userId, TypeItems typeItems) {
        if (listMap.containsKey(userId)) {
            listMap.get(userId).add(typeItems);
            return listMap.get(userId).size();
        } else {
            List<TypeItems> list = new LinkedList<>();
            list.add(typeItems);
            listMap.put(userId, list);
            return 1;
        }
    }
}
