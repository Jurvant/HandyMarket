package ilya.pon.search.util;

import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import ilya.pon.search.dto.SearchRequestDto;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.util.List;

public final class SearchUtil {

    private SearchUtil() {}

    public static Query getQueryBuilder(final SearchRequestDto dto){
        if(dto == null){
            return null;
        }

        List<String> fields = dto.fields();
        if(fields == null || fields.isEmpty()) {return null;}

        if(fields.size() > 1){

            Query query = MultiMatchQuery.of(m -> m
                    .fields(fields)
                    .query(dto.searchTerm())
            )._toQuery();

        }


        return null;
    }
}
