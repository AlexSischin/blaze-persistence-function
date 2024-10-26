package com.example.blaze.persistence.function.config.function;

import com.blazebit.persistence.spi.FunctionRenderContext;
import com.blazebit.persistence.spi.JpqlFunction;

public class GetCatFunction implements JpqlFunction {

    @Override
    public boolean hasArguments() {
        return true;
    }

    @Override
    public boolean hasParenthesesIfNoArguments() {
        return true;
    }

    @Override
    public Class<?> getReturnType(Class<?> aClass) {
        return aClass;
    }

    @Override
    public void render(FunctionRenderContext context) {
        context.addChunk("SELECT * FROM get_cat_id(");
        context.addArgument(0);
        context.addChunk(")");
    }
}
