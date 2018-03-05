package com.qxcmp.spdier;

import lombok.Getter;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 蜘蛛管道
 * <p>
 * 负责对页面处理器解析后的领域实体对象进行进一步处理，比如入库等等
 *
 * @param <T>
 *
 * @author aaric
 * @see SpiderPageProcessor
 */
public abstract class SpiderPipeline<T> implements Pipeline {

    /**
     * 处理领域对象实体总数
     */
    @Getter
    private long totalCount;

    /**
     * 处理新的领域对象数量
     */
    @Getter
    private long newCount;

    /**
     * 处理有修改的领域对象数量
     */
    @Getter
    private long updateCount;

    /**
     * 未处理的领域对象数量
     */
    @Getter
    private long dropCount;

    /**
     * 管道处理逻辑
     *
     * @param resultItems 页面处理器传过来的参数，包涵了领域实体对象
     * @param task        蜘蛛任务
     */
    @Override
    public final void process(ResultItems resultItems, Task task) {
        T target = checkNotNull(resultItems.get(SpiderPageProcessor.PIPELINE_RESULT));

        if (!isValidTarget(target)) {
            return;
        }

        ++totalCount;

        Optional<T> origin = getOriginTarget(target);

        if (origin.isPresent()) {
            if (isTargetChanged(target, origin.get())) {
                updateTarget(target, origin.get());
                ++updateCount;
            } else {
                dropTarget(target);
                ++dropCount;
            }
        } else {
            newTarget(target);
            ++newCount;
        }

    }

    /**
     * 检查领域对象是否有效
     *
     * @param target 领域对象
     *
     * @return 如果领域对象无效，则抛出{@link IllegalStateException}
     */
    protected abstract boolean isValidTarget(T target);

    /**
     * 根据抓取到的领域对象来获取原始领域对象
     * <p>
     * 该方法用来判断抓取到的领域对象是否为新的对象
     *
     * @param target 领域对象
     *
     * @return 如果没有原始领域对象返回 {@link Optional#empty()}
     */
    protected abstract Optional<T> getOriginTarget(T target);

    /**
     * 如果原始领域对象存在，则会调用该方法来判断新旧领域对象是否有变化
     *
     * @param target 新的领域对象
     * @param origin 原始领域对象
     *
     * @return 领域对象是否有变化
     */
    protected abstract boolean isTargetChanged(T target, T origin);

    /**
     * 该方法处理领域对象为新的情况，一般负责领域对象入库
     *
     * @param target 新的领域对象
     */
    protected abstract void newTarget(T target);

    /**
     * 该方法处理领域对象有变化的情况，一般负责更新领域对象并入库
     *
     * @param target 新的领域对象
     * @param origin 原始领域对象
     */
    protected abstract void updateTarget(T target, T origin);

    /**
     * 该方法处理没有变化的领域对象
     *
     * @param target 没有变化的领域对象
     */
    protected abstract void dropTarget(T target);
}
