/*
 * Licensed to Crate under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Crate licenses this file
 * to you under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial
 * agreement.
 */

package io.crate.analyze.symbol;

import io.crate.metadata.Reference;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class RefReplacer extends FunctionCopyVisitor<Function<? super Reference, ? extends Symbol>> {

    private final static RefReplacer REPLACER = new RefReplacer();

    private RefReplacer() {
        super();
    }

    public static io.crate.analyze.symbol.Function replaceRefs(io.crate.analyze.symbol.Function func,
                                                               Function<? super Reference, ? extends Symbol> replaceFunc) {
        return (io.crate.analyze.symbol.Function) REPLACER.process(func, replaceFunc);
    }

    public static Symbol replaceRefs(Symbol tree, Function<? super Reference, ? extends Symbol> replaceFunc) {
        return REPLACER.process(tree, replaceFunc);
    }

    @Override
    public Symbol visitReference(Reference ref, Function<? super Reference, ? extends Symbol> replaceFunc) {
        return requireNonNull(replaceFunc.apply(ref), "mapper function used in RefReplacer must not return null values");
    }
}
