# BaseMVVM

Có 1 rule quan trọng trong MVVM là ViewModel(VM) không được giữ instance của View(V).
Vì VM tồn tại lâu hơn V nên vi phạm điều này sẽ gây ra memory leaks.

Chúng ta cần hiểu rằng không có 1 architecture nào là hoàn hảo cả. Những lợi ích khác nhau sẽ phải đánh đổi với những bất lợi khác nhau.
Để chọn ra architecture
phù hợp chúng ta cần xem xét:
  - View có độ phức tạp như thế nào?
  - Viết UnitTest đến mức độ nào?
  - Khả năng tái sử dụng và trừu tượng đến mức độ nào?
  
Ví dụ như bạn có View phức tạp thì nên sử dụng MVP thay vì MVVM, vì trong MVP thì Presenter 
giữ instance của View nên dễ dàng trong việc triển khai presentation logic hơn MVVM.

Ngay cả khi bạn quyết định chọn MVVM thì bạn cũng phải cân nhắc cách triển khai như thế nào cho từng trường hợp: 
  - Nếu bạn muốn VM được tái sử dụng ở nhiều View thì bạn sẽ phải đánh đổi là đẩy kha khá presentation logic
sang View (dẫn đến View phức tạp hơn, viết UnitTest khó khăn hơn vì dính đến Framework) để VM chỉ có nhiệm vụ get và notify data. Như sau:

```javascript
class SampleVM @Inject constructor(
    private val sampleUseCase: ISampleUseCase
) : BaseVM() {

    private val _sampleData = MutableLiveData<Resource<SampleData>>()
    val sampleData: LiveData<Resource<SampleData>> = _sampleData

    fun fetchSampleContent() {
        viewModelScope.launch {
            sampleUseCase.getSample()
                .collect { resource ->
                    launch(Dispatchers.Main.immediate) {
                        _sampleData.value = resource.getResource()
                    }
                }
        }
    }
}

class SampleScreen : BaseFragmentX<SampleVM>() {

    override val viewModel: SampleVM by viewModels({this}, {viewModelFactory})

    override fun initData(argument: Bundle?) {
        viewModel.fetchSampleContent()
        viewModel.sampleData.observe(this, Observer<Resource<SampleData>> { resource ->
            // Khá nhiều presentation logic sau khi nhận được data
            if (resource.data != null) {
                if (resource.data.items?.isNotEmpty() == true) {
                    // do something
                } else {
                    // do something
                }
            } else {
                // do something
            }
        })
    }

}
```
  - Nếu muốn View đơn giản hơn, viết UnitTest cho presentation logic hiệu quả hơn thì presentation logic k nên được implement ở View như ví dụ trên, mà
nên được implement ở VM, điều này thì lại phải đánh đổi bằng việc VM sẽ khó tái sử dụng ở nhiều View khác
vì đang chứa presentaion logic của View này rồi. Cách triển khai này như sau:

```javascript
class SampleVM @Inject constructor(
    private val sampleUseCase: ISampleUseCase
) : BaseVM() {

    private val _sampleData = MutableLiveData<Resource<SampleData>>()
    val sampleData: LiveData<Resource<SampleData>> = _sampleData

    private val _dataTitle = MutableLiveData<String?>()
    val dataTitle: LiveData<String?> = _dataTitle

    private val _dataRecyclerView = MutableLiveData<List<Item>?>()
    val dataRecyclerView: LiveData<List<Item>?> = _dataRecyclerView

    fun fetchSampleContent() {
        viewModelScope.launch {
            sampleUseCase.getSample()
                .collect { resource ->
                    launch(Dispatchers.Main.immediate) {
                        _sampleData.value = resource.getResource()
                        _dataTitle.value = resource.data?.title
                        _dataRecyclerView.value = resource.data?.items
                    }
                }
        }
    }
}

class SampleScreen : BaseFragmentX<SampleVM>() {

    override val viewModel: SampleVM by viewModels({this}, {viewModelFactory})

    override fun initData(argument: Bundle?) {
        viewModel.fetchSampleContent()
        viewModel.dataTitle.observe(this, Observer { data ->
            setTittleScreen(data ?: "")
        })
        viewModel.dataRecyclerView.observe(this, Observer { data ->
            (recyclerView.adapter as? SampleAdapter)?.setData(data)
        })
    }
}
```

Thực tế thì bản thân mình ít khi tái sử dụng lại VM. Vậy nên mình thường triển khai VM theo cách thứ 2.
